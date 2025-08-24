package ru.yandex.practicum.filmorate.validations;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Map;

@Slf4j
public class FilmValidation {
    private static final LocalDate MIN_DATE_RELEASE = LocalDate.of(1895, 12, 28);

    public static void validateDuplicatedFilm(Film film, Map<Integer, Film> films) {
        if (films.values().stream()
                .anyMatch(filmFromData -> filmFromData.equals(film))) {
            log.warn("Проверка на копию не пройдена, фильм с таким названием: {} ,уже есть ", film.getName());
            throw new DuplicatedDataException("Вы пытаетесь добавить уже существующий фильм");
        }

    }

    public static void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("В отправленной форме отсутствует имя");
            throw new ValidationException("Имя фильма не может быть пустым");
        }
        int descriptionMaxLength = 200;
        if (film.getDescription() == null || film.getDescription().length() > descriptionMaxLength) {
            log.warn("Описание фильма задана не верно {}", film.getDescription());
            throw new ValidationException("Некорректное описание");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(MIN_DATE_RELEASE)) {
            log.warn("В отправленной форме неверно указана дата релиза: {}", film.getReleaseDate());
            throw new ValidationException("Неверно указана дата релиза");
        }
        if (film.getDuration() <= 0) {
            log.warn("Неверно указана продолжительность: {}", film.getDuration());
            throw new ValidationException("Продолжительность фильма должна быть больше нуля");
        }
    }
}
