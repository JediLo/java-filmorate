package ru.yandex.practicum.filmorate.validations;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.DuplicatedDataException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

@Slf4j
public class ValidationDuplicate {
    public static void validateDuplicateUser(User user, Map<Integer, User> users) {
        if (users.values().stream()
                .anyMatch(userFromData -> (userFromData.equals(user) && userFromData.getId() != user.getId()))) {
            log.warn("Такая почта: {} уже заведена в базу, запрос отклонен", user.getEmail());
            throw new DuplicatedDataException("Пользователь с такой почтой уже есть");
        }
    }

    public static void validateDuplicatedFilm(Film film, Map<Integer, Film> films) {
        if (films.values().stream()
                .anyMatch(filmFromData -> filmFromData.equals(film))) {
            log.warn("Проверка на копию не пройдена, фильм с таким названием: {} ,уже есть ", film.getName());
            throw new DuplicatedDataException("Вы пытаетесь добавить уже существующий фильм");
        }

    }
}
