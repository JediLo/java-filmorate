package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;


import java.time.LocalDate;

/**
 * Film.
 */
@Data
@EqualsAndHashCode(exclude = "id")
@Builder
public class Film {
    int id;
    String name;
    String description;
    LocalDate releaseDate;
    int duration;
}
