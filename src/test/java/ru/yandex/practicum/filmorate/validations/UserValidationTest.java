package ru.yandex.practicum.filmorate.validations;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.filmorate.exceptions.DuplicatedDataException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.yandex.practicum.filmorate.validations.ValidationDuplicate.validateDuplicateUser;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ValidationAutoConfiguration.class)
class UserValidationTest {
    @Autowired
    Validator validator;

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
        Set<ConstraintViolation<User>> constraintViolations =
                validator.validate(user);
        assertEquals(1, constraintViolations.size());
        assertEquals("Электронная почта не может быть пустой",
                constraintViolations.iterator().next().getMessage());
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
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());
        assertEquals("Электронная почта должна содержать символ @",
                constraintViolations.iterator().next().getMessage());
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
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());
        assertEquals("Электронная почта не может быть пустой",
                constraintViolations.iterator().next().getMessage());
    }

    @Test
    void shouldThrowValidationWhenLoginIsNull() {
        User user = User.builder()
                .id(1)
                .email("email@gmail.com")
                .name("Name")
                .birthday(LocalDate.of(2025, 1, 1))
                .build();
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());
        assertEquals("Логин не может быть пустым",
                constraintViolations.iterator().next().getMessage());
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
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());
        assertEquals("Логин не может быть пустым",
                constraintViolations.iterator().next().getMessage());
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
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());
        assertEquals("Логин не может содержать пробелы",
                constraintViolations.iterator().next().getMessage());
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
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());
        assertEquals("Дата рождения должна быть в прошлом",
                constraintViolations.iterator().next().getMessage());
    }

    @Test
    void shouldThrowValidationWhenBirthdayIsNull() {
        User user = User.builder()
                .id(1)
                .email("email@gmail.com")
                .login("login")
                .name("Name")
                .build();
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());
        assertEquals("Дата рождения должна быть заполнена",
                constraintViolations.iterator().next().getMessage());
    }
}