package ru.yandex.practicum.filmorate.validations;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Map;

@Slf4j
public class UserValidation {
    public static void validateDuplicateUser(User user, Map<Integer,User> users) {
        if (users.values().stream()
                .anyMatch(userFromData -> (userFromData.equals(user) && userFromData.getId() != user.getId()))) {
            log.warn("Такая почта: {} уже заведена в базу, запрос отклонен", user.getEmail());
            throw new DuplicatedDataException("Пользователь с такой почтой уже есть");
        }
    }

    public static void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Неверно указана почта: {}", user.getEmail());
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Неверно заполнен логин: {}", user.getLogin());
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Неверно указана дата рождения: {}", user.getBirthday());
            throw new ValidationException("Неверно заполнена дата рождения");
        }

    }
}
