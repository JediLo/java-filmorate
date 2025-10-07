package ru.yandex.practicum.filmorate.model;


import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private int id;
    @EqualsAndHashCode.Include
    @NotBlank(message = "Электронная почта не может быть пустой")
    @Email(message = "Электронная почта должна содержать символ @")
    private String email;
    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "^[^ ]*$", message = "Логин не может содержать пробелы")
    private String login;
    private String name;
    @NotNull(message = "Дата рождения должна быть заполнена")
    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthday;
    private Set<Integer> likesFilms;
    private Set<Integer> friendSet;


    public boolean addFriend(Integer id) {
        return friendSet.add(id);
    }

    public void removeFriend(int id) {
        friendSet.remove(id);
    }
}
