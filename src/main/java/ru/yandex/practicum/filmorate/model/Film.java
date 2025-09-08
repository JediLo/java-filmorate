package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validations.ReleaseDateAfter;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
@EqualsAndHashCode(exclude = "id")
@Builder
public class Film {
    int id;
    @NotBlank(message = "Имя не может быть пустым")
    String name;
    @NotBlank(message = "Описание не может быть пустым")
    @Size(max = 200, message = "Описание не может быть более 200 символов")
    String description;
    @NotNull(message = "Дата Релиза фильма не может быть пустой")
    @ReleaseDateAfter(value = "1895-12-28", message = "Дата релиза фильма не может быть раньше 1895-12-28")
    LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должно быть положительным")
    int duration;
}
