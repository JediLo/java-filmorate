package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.yandex.practicum.filmorate.validations.ReleaseDateAfter;

import java.time.LocalDate;
import java.util.Set;


/**
 * Film.
 */
@Data
@EqualsAndHashCode(exclude = "id")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Film {
    private int id;
    @NotBlank(message = "Имя не может быть пустым")
    private String name;
    @NotBlank(message = "Описание не может быть пустым")
    @Size(max = 200, message = "Описание не может быть более 200 символов")
    private String description;
    @NotNull(message = "Дата Релиза фильма не может быть пустой")
    @ReleaseDateAfter(value = "1895-12-28", message = "Дата релиза фильма не может быть раньше 1895-12-28")
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должно быть положительным")
    private int duration;
    private MpaFilm mpa;
    private Set<Genre> genres;
    private Set<Integer> likesUsers;


}
