package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class User {
    int id;
    @EqualsAndHashCode.Include
    String email;

    String login;
    String name;
    LocalDate birthday;
}
