package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.yandex.practicum.filmorate.validations.ReleaseDateAfter;

import java.time.LocalDate;
import java.util.HashSet;
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

    Set<Integer> likesUsers = new HashSet<>();

    public boolean addLike(int id) {
        return likesUsers.add(id);
    }

    public boolean removeLike(int id) {
        return likesUsers.remove(id);
    }

}
