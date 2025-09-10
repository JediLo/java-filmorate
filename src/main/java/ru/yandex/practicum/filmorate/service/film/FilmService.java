package ru.yandex.practicum.filmorate.service.film;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.Comparator;


@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Collection<Film> findAllFilms() {
        log.info("Запрос на получение всех фильмов");
        return filmStorage.findAllFilms();
    }

    public Film addFilm(Film film) {
        log.info("Попытка добавления фильма {}", film.getName());
        validateDuplicatedFilm(film);
        film.setId(getNextId());
        Film saved = filmStorage.addFilm(film);
        log.info("Фильм с ID: {}, был успешно добавлен", film.getId());
        return saved;
    }


    public Film updateFilm(Film film) {
        log.info("Попытка изменения фильма с ID: {}", film.getId());
        validateDuplicatedFilm(film);
        Film updated = filmStorage.updateFilm(updateFilmData(film));
        log.info("Фильм с ID: {}, был успешно изменен", film.getId());
        return updated;
    }

    public Film getExistingFilmById(int id) {
        log.info("Попытка получения фильма с ID: {}", id);
        Film filmFromData = filmStorage.getFilmByID(id);
        if (filmFromData == null) {
            log.warn("В системе нет фильма с переданным ID: {}", id);
            throw new NotFoundException("Фильм с таким ID не найден");
        }
        log.info("Фильм с ID: {} успешно получен.", filmFromData.getId());
        return filmFromData;
    }

    private Integer getNextId() {
        int currentMaxId = filmStorage.findAllFilms().stream()
                .mapToInt(Film::getId)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void validateDuplicatedFilm(Film film) {
        if (filmStorage.findAllFilms().stream()
                .anyMatch(filmFromData -> filmFromData.equals(film))) {
            log.warn("Проверка на копию не пройдена, фильм с таким названием: {} ,уже есть ", film.getName());
            throw new NotFoundException("Вы пытаетесь добавить уже существующий фильм");
        }
    }

    private Film updateFilmData(Film newFilm) {
        log.info("Попытка обновления данных фильма");
        Film filmFromMemory = filmStorage.getFilmByID(newFilm.getId());
        filmFromMemory.setName(newFilm.getName());
        filmFromMemory.setDescription(newFilm.getDescription());
        filmFromMemory.setDuration(newFilm.getDuration());
        filmFromMemory.setReleaseDate(newFilm.getReleaseDate());
        log.info("Данные фильма успешно обновлены.");
        return filmFromMemory;
    }

    public Film addLike(int id, int userId) {
        log.info("Попытка поставить лайк фильму с ID: {}, пользователем с ID: {}", id, userId);
        Film film = getExistingFilmById(id);
        userService.getExistingUserById(userId);
        if (!film.addLike(userId)) {
            throw new DuplicatedDataException("Лайк уже был поставлен ранее");
        } else {
            log.info("Лайк у фильма с ID: {}, был добавлен пользователем с ID:{}", id, userId);
            return film;
        }
    }

    public Film removeLike(int id, int userId) {
        log.info("Попытка убрать лайк у фильма с ID: {}, пользователем с ID: {}", id, userId);
        Film film = getExistingFilmById(id);
        userService.getExistingUserById(userId);
        if (!film.removeLike(userId)) {
            throw new DuplicatedDataException("Нет лайка который вы хотели бы убрать");
        } else {
            log.info("Лайк у фильма с ID: {}, был убран пользователем с ID:{}", id, userId);
            return film;
        }
    }

    public Collection<Film> findPopularFilms(int count) {
        log.info("Попытка получения {} лучших фильмов", count);
        if (count < 1) {
            throw new ValidationException("Количество фильмов не может быть отрицательным");
        }
        log.info("Получен список лучших {} фильмов", count);
        return filmStorage.findAllFilms().stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getLikesUsers().size()).reversed())
                .limit(count)
                .toList();

    }
}
