package ru.yandex.practicum.filmorate.validations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.validations.UserValidation.validateDuplicateUser;
import static ru.yandex.practicum.filmorate.validations.UserValidation.validateUser;

class UserValidationTest {

    @Test
    void shouldThrowDuplicateWhenEmailExist() {
        Map<Integer, User> users = new HashMap<>();
        // Создаем Пользователя и добавляем в наш Map
        User user = User.builder()
                .id(1)
                .email("email@gmail.com")
                .login("login")
                .name("Name")
                .birthday(LocalDate.of(2025, 1, 1))
                .build();
        users.put(user.getId(), user);
        // Создаем второго пользователя с такой же почтой
        User user2 = User.builder()
                .id(2)
                .email("email@gmail.com")
                .login("login2")
                .name("Name2")
                .birthday(LocalDate.of(2025, 2, 2))
                .build();
        DuplicatedDataException duplicatedDataException =
                Assertions.assertThrows(DuplicatedDataException.class, () -> validateDuplicateUser(user2, users));
        Assertions.assertEquals("Пользователь с такой почтой уже есть", duplicatedDataException.getMessage());
    }

    @Test
    void shouldThrowValidationWhenEmailIsNull() {
        User user = User.builder()
                .id(1)
                .login("login")
                .name("Name")
                .birthday(LocalDate.of(2025, 1, 1))
                .build();
        ValidationException validationException =
                Assertions.assertThrows(ValidationException.class, () -> validateUser(user));
        Assertions.assertEquals("Электронная почта не может быть пустой и должна содержать символ @",
                validationException.getMessage());
    }

    @Test
    void shouldThrowValidationWhenEmailWithoutAtSign() {
        User user = User.builder()
                .id(1)
                .email("emailgmail.com")
                .login("login")
                .name("Name")
                .birthday(LocalDate.of(2025, 1, 1))
                .build();
        ValidationException validationException =
                Assertions.assertThrows(ValidationException.class, () -> validateUser(user));
        Assertions.assertEquals("Электронная почта не может быть пустой и должна содержать символ @",
                validationException.getMessage());
    }

    @Test
    void shouldThrowValidationWhenEmailIsBlank() {
        User user = User.builder()
                .id(1)
                .email("")
                .login("login")
                .name("Name")
                .birthday(LocalDate.of(2025, 1, 1))
                .build();
        ValidationException validationException =
                Assertions.assertThrows(ValidationException.class, () -> validateUser(user));
        Assertions.assertEquals("Электронная почта не может быть пустой и должна содержать символ @",
                validationException.getMessage());
    }

    @Test
    void shouldThrowValidationWhenLoginIsNull() {
        User user = User.builder()
                .id(1)
                .email("email@gmail.com")
                .name("Name")
                .birthday(LocalDate.of(2025, 1, 1))
                .build();
        ValidationException validationException =
                Assertions.assertThrows(ValidationException.class, () -> validateUser(user));
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы",
                validationException.getMessage());
    }

    @Test
    void shouldThrowValidationWhenLoginIsBlank() {
        User user = User.builder()
                .id(1)
                .email("email@gmail.com")
                .login("")
                .name("Name")
                .birthday(LocalDate.of(2025, 1, 1))
                .build();
        ValidationException validationException =
                Assertions.assertThrows(ValidationException.class, () -> validateUser(user));
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы",
                validationException.getMessage());
    }

    @Test
    void shouldThrowValidationWhenLoginWithSpace() {
        User user = User.builder()
                .id(1)
                .email("email@gmail.com")
                .login("login with space")
                .name("Name")
                .birthday(LocalDate.of(2025, 1, 1))
                .build();
        ValidationException validationException =
                Assertions.assertThrows(ValidationException.class, () -> validateUser(user));
        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы",
                validationException.getMessage());
    }

    @Test
    void shouldThrowValidationWhenBirthdayInFuture() {
        User user = User.builder()
                .id(1)
                .email("email@gmail.com")
                .login("login")
                .name("Name")
                .birthday(LocalDate.of(2026, 1, 1))
                .build();
        ValidationException validationException =
                Assertions.assertThrows(ValidationException.class, () -> validateUser(user));
        Assertions.assertEquals("Неверно заполнена дата рождения",
                validationException.getMessage());
    }

    @Test
    void shouldThrowValidationWhenBirthdayIsNull() {
        User user = User.builder()
                .id(1)
                .email("email@gmail.com")
                .login("login")
                .name("Name")
                .build();
        ValidationException validationException =
                Assertions.assertThrows(ValidationException.class, () -> validateUser(user));
        Assertions.assertEquals("Неверно заполнена дата рождения",
                validationException.getMessage());
    }
}