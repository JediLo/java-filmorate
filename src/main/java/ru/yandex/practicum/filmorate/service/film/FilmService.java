package ru.yandex.practicum.filmorate.service.film;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;


@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;

    public FilmService(@Qualifier("bd") FilmStorage filmStorage, GenreStorage genreStorage, MpaStorage mpaStorage) {
        this.filmStorage = filmStorage;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
    }

    public Collection<Film> findAllFilms() {
        log.info("Запрос на получение всех фильмов");
        return filmStorage.findAllFilms();
    }

    public Film addFilm(Film film) {
        log.info("Попытка добавления фильма {}", film.getName());
        validateDuplicatedFilm(film);
        validateExistRatingAndGenre(film);
        Film saved = filmStorage.addFilm(film);
        log.info("Фильм с ID: {}, был успешно добавлен", film.getId());
        return saved;
    }


    public Film updateFilm(Film film) {
        log.info("Попытка изменения фильма с ID: {}", film.getId());
        validateDuplicatedFilm(film);
        validateExistRatingAndGenre(film);
        Film updated = filmStorage.updateFilm(updateFilmData(film));
        log.info("Фильм с ID: {}, был успешно изменен", film.getId());
        return updated;
    }


    public Film getExistingFilmById(int id) {
        log.info("Попытка получения фильма с ID: {}", id);
        Optional<Film> filmFromData = filmStorage.getFilmByID(id);
        if (filmFromData.isEmpty()) {
            log.warn("В системе нет фильма с переданным ID: {}", id);
            throw new NotFoundException("Фильм с таким ID не найден");
        }
        log.info("Фильм с ID: {} успешно получен.", id);
        return filmFromData.get();
    }


    private void validateDuplicatedFilm(Film film) {
        if (filmStorage.findAllFilms().stream()
                .anyMatch(filmFromData -> filmFromData.equals(film))) {
            log.warn("Проверка на копию не пройдена, фильм с таким названием: {} ,уже есть ", film.getName());
            throw new NotFoundException("Вы пытаетесь добавить уже существующий фильм");
        }
    }

    private void validateExistRatingAndGenre(Film film) {
        if (!mpaStorage.mpaExist(film.getMpa().getId())) {
            log.warn("В базе данных нет рейтинга с ID {}", film.getMpa().getId());
            throw new NotFoundException("У фильма неверно указан рейтинг");
        }
        if (!genreStorage.genresExist(film.getGenres())) {
            log.warn("В базе данных нет одного из жанров {}", film.getGenres());
            throw new NotFoundException("У фильма неверно задан(ы) жанр(ы)");
        }
    }

    private Film updateFilmData(Film newFilm) {
        log.info("Попытка обновления данных фильма");
        Optional<Film> film = filmStorage.getFilmByID(newFilm.getId());
        if (film.isEmpty()) {
            log.warn("В системе нет фильма, который обновляется с  ID: {}", newFilm.getId());
            throw new NotFoundException("Фильм с таким ID не найден");
        } else {
            Film filmFromData = filmStorage.updateFilm(newFilm);
            log.info("Данные фильма успешно обновлены.");
            return filmFromData;
        }
    }

    public void addLike(int filmId, int userId) {
        log.info("Попытка поставить лайк фильму с ID: {}, пользователем с ID: {}", filmId, userId);
        filmStorage.addLike(filmId, userId);
        //if (!film.addLike(userId)) {
        //   throw new DuplicatedDataException("Лайк уже был поставлен ранее");
        // }
        log.info("Лайк у фильма с ID: {}, был добавлен пользователем с ID:{}", filmId, userId);


    }

    public void removeLike(int filmId, int userId) {
        log.info("Попытка убрать лайк у фильма с ID: {}, пользователем с ID: {}", filmId, userId);
        filmStorage.removeLike(filmId, userId);
        //if (!film.removeLike(userId)) {
        //    throw new DuplicatedDataException("Нет лайка который вы хотели бы убрать");
        //}
        log.info("Лайк у фильма с ID: {}, был убран пользователем с ID:{}", filmId, userId);


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
