package ru.yandex.practicum.filmorate.model;


import jakarta.validation.constraints.*;
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
    @NotBlank(message = "Электронная почта не может быть пустой")
    @Email(message = "Электронная почта должна содержать символ @")
    String email;
    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "^[^ ]*$", message = "Логин не может содержать пробелы")
    String login;
    String name;
    @NotNull(message = "Дата рождения должна быть заполнена")
    @Past(message = "Дата рождения должна быть в прошлом")
    LocalDate birthday;
}
