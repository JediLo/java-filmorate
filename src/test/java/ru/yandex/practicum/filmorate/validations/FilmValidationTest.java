package ru.yandex.practicum.filmorate.validations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.validations.FilmValidation.validateDuplicatedFilm;
import static ru.yandex.practicum.filmorate.validations.FilmValidation.validateFilm;

class FilmValidationTest {

    @Test
    void shouldThrowDuplicateWhenFilmDataIsIdenticalExceptId() {
        Map<Integer, Film> films = new HashMap<>();
        // Создаем фильм и добавляем в Map
        Film film = Film.builder()
                .id(1)
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(2025, 1, 1))
                .duration(Duration.ofMinutes(105))
                .build();
        films.put(film.getId(), film);
        // Создаем второй фильм с точно такими же параметрами за исключением ID
        Film film2 = Film.builder()
                .id(2)
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(2025, 1, 1))
                .duration(Duration.ofMinutes(105))
                .build();
        DuplicatedDataException duplicatedDataException =
                Assertions.assertThrows(DuplicatedDataException.class, () -> validateDuplicatedFilm(film2, films));
        Assertions.assertEquals("Вы пытаетесь добавить уже существующий фильм",
                duplicatedDataException.getMessage());
    }

    @Test
    void shouldThrowValidationWhenNameIsBlank() {
        Film film = Film.builder()
                .id(1)
                .name("")
                .description("Description")
                .releaseDate(LocalDate.of(2025, 1, 1))
                .duration(Duration.ofMinutes(105))
                .build();
        ValidationException validationException =
                Assertions.assertThrows(ValidationException.class, () -> validateFilm(film));
        Assertions.assertEquals("Имя фильма не может быть пустым", validationException.getMessage());
    }

    @Test
    void shouldThrowValidationWhenNameIsNull() {
        Film film = Film.builder()
                .id(1)
                .description("Description")
                .releaseDate(LocalDate.of(2025, 1, 1))
                .duration(Duration.ofMinutes(105))
                .build();
        ValidationException validationException =
                Assertions.assertThrows(ValidationException.class, () -> validateFilm(film));
        Assertions.assertEquals("Имя фильма не может быть пустым", validationException.getMessage());
    }

    @Test
    void shouldThrowValidationWhenDescriptionIsNull() {
        Film film = Film.builder()
                .id(1)
                .name("Name")
                .releaseDate(LocalDate.of(2025, 1, 1))
                .duration(Duration.ofMinutes(105))
                .build();
        ValidationException validationException =
                Assertions.assertThrows(ValidationException.class, () -> validateFilm(film));
        Assertions.assertEquals("Некорректное описание", validationException.getMessage());
    }

    @Test
    void shouldThrowValidationWhenDescriptionExceedsMaxLength() {
        Film film = Film.builder()
                .id(1)
                .name("Name")
                .description("Description Exceeds Max Length".repeat(7))
                .releaseDate(LocalDate.of(2025, 1, 1))
                .duration(Duration.ofMinutes(105))
                .build();
        ValidationException validationException =
                Assertions.assertThrows(ValidationException.class, () -> validateFilm(film));
        Assertions.assertEquals("Некорректное описание", validationException.getMessage());
    }

    @Test
    void shouldThrowValidationWhenDateReleaseIsNull() {
        Film film = Film.builder()
                .id(1)
                .name("Name")
                .description("Description")
                .duration(Duration.ofMinutes(105))
                .build();
        ValidationException validationException =
                Assertions.assertThrows(ValidationException.class, () -> validateFilm(film));
        Assertions.assertEquals("Неверно указана дата релиза", validationException.getMessage());
    }

    @Test
    void shouldThrowValidationWhenDateReleaseIsBeforeMinDateRelease() {
        Film film = Film.builder()
                .id(1)
                .name("Name")
                .description("Description")
                .duration(Duration.ofMinutes(105))
                .releaseDate(LocalDate.of(1895, 12, 27))
                .build();
        ValidationException validationException =
                Assertions.assertThrows(ValidationException.class, () -> validateFilm(film));
        Assertions.assertEquals("Неверно указана дата релиза", validationException.getMessage());
    }

    @Test
    void shouldThrowValidationWhenDurationIsNull() {
        Film film = Film.builder()
                .id(1)
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(2025, 1, 1))
                .build();
        ValidationException validationException =
                Assertions.assertThrows(ValidationException.class, () -> validateFilm(film));
        Assertions.assertEquals("Продолжительность фильма должна быть больше нуля",
                validationException.getMessage());
    }

    @Test
    void shouldThrowValidationWhenDurationIsZero() {
        Film film = Film.builder()
                .id(1)
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(2025, 1, 1))
                .duration(Duration.ZERO)
                .build();
        ValidationException validationException =
                Assertions.assertThrows(ValidationException.class, () -> validateFilm(film));
        Assertions.assertEquals("Продолжительность фильма должна быть больше нуля",
                validationException.getMessage());
    }

    @Test
    void shouldThrowValidationWhenDurationIsNegative() {
        Film film = Film.builder()
                .id(1)
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(2025, 1, 1))
                .duration(Duration.ofMinutes(-1))
                .build();
        ValidationException validationException =
                Assertions.assertThrows(ValidationException.class, () -> validateFilm(film));
        Assertions.assertEquals("Продолжительность фильма должна быть больше нуля",
                validationException.getMessage());
    }
}