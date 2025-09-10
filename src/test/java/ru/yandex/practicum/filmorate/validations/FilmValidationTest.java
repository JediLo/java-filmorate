package ru.yandex.practicum.filmorate.validations;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ValidationAutoConfiguration.class)
class FilmValidationTest {
    @Autowired
    private Validator validator;


    @Test
    void shouldThrowValidationWhenNameIsBlank() {
        Film film = Film.builder()
                .id(1)
                .name("")
                .description("Description")
                .releaseDate(LocalDate.of(2025, 1, 1))
                .duration(105)
                .build();
        Set<ConstraintViolation<Film>> constraintViolations =
                validator.validate(film);
        assertEquals(1, constraintViolations.size());
        assertEquals("Имя не может быть пустым", constraintViolations.iterator().next().getMessage());
    }

    @Test
    void shouldThrowValidationWhenNameIsNull() {
        Film film = Film.builder()
                .id(1)
                .description("Description")
                .releaseDate(LocalDate.of(2025, 1, 1))
                .duration(105)
                .build();
        Set<ConstraintViolation<Film>> constraintViolations =
                validator.validate(film);
        assertEquals(1, constraintViolations.size());
        assertEquals("Имя не может быть пустым", constraintViolations.iterator().next().getMessage());
    }

    @Test
    void shouldThrowValidationWhenDescriptionIsNull() {
        Film film = Film.builder()
                .id(1)
                .name("Name")
                .releaseDate(LocalDate.of(2025, 1, 1))
                .duration(105)
                .build();
        Set<ConstraintViolation<Film>> constraintViolations =
                validator.validate(film);
        assertEquals(1, constraintViolations.size());
        assertEquals("Описание не может быть пустым", constraintViolations.iterator().next().getMessage());
    }

    @Test
    void shouldThrowValidationWhenDescriptionExceedsMaxLength() {


        Film film = Film.builder()
                .id(1)
                .name("Name")
                .description("Description Exceeds Max Length".repeat(7))
                .releaseDate(LocalDate.of(2025, 1, 1))
                .duration(105)
                .build();
        Set<ConstraintViolation<Film>> constraintViolations =
                validator.validate(film);
        assertEquals(1, constraintViolations.size());
        assertEquals("Описание не может быть более 200 символов", constraintViolations.iterator().next().getMessage());

    }

    @Test
    void shouldThrowValidationWhenDateReleaseIsNull() {
        Film film = Film.builder()
                .id(1)
                .name("Name")
                .description("Description")
                .duration(105)
                .build();
        Set<ConstraintViolation<Film>> constraintViolations =
                validator.validate(film);
        assertEquals(1, constraintViolations.size());
        assertEquals("Дата Релиза фильма не может быть пустой", constraintViolations.iterator().next().getMessage());
    }

    @Test
    void shouldThrowValidationWhenDateReleaseIsBeforeMinDateRelease() {
        Film film = Film.builder()
                .id(1)
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(1895, 12, 27))
                .duration(105)
                .build();
        Set<ConstraintViolation<Film>> constraintViolations =
                validator.validate(film);
        assertEquals(1, constraintViolations.size());
        assertEquals("Дата релиза фильма не может быть раньше 1895-12-28", constraintViolations.iterator().next().getMessage());
    }

    @Test
    void shouldThrowValidationWhenDurationIsNull() {
        Film film = Film.builder()
                .id(1)
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(2025, 1, 1))
                .build();
        Set<ConstraintViolation<Film>> constraintViolations =
                validator.validate(film);
        assertEquals(1, constraintViolations.size());
        assertEquals("Продолжительность фильма должно быть положительным", constraintViolations.iterator().next().getMessage());
    }

    @Test
    void shouldThrowValidationWhenDurationIsZero() {
        Film film = Film.builder()
                .id(1)
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(2025, 1, 1))
                .duration(0)
                .build();
        Set<ConstraintViolation<Film>> constraintViolations =
                validator.validate(film);
        assertEquals(1, constraintViolations.size());
        assertEquals("Продолжительность фильма должно быть положительным", constraintViolations.iterator().next().getMessage());
    }

    @Test
    void shouldThrowValidationWhenDurationIsNegative() {
        Film film = Film.builder()
                .id(1)
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(2025, 1, 1))
                .duration(-1)
                .build();
        Set<ConstraintViolation<Film>> constraintViolations =
                validator.validate(film);
        assertEquals(1, constraintViolations.size());
        assertEquals("Продолжительность фильма должно быть положительным", constraintViolations.iterator().next().getMessage());
    }
}