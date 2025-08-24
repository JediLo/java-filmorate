package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.validations.UserValidation.validateDuplicateUser;
import static ru.yandex.practicum.filmorate.validations.UserValidation.validateUser;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAllUsers() {
        log.info("Запрос на получение всех пользователей");
        return users.values();
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        log.info("Попытка добавления пользователя с ID: {}", user.getId());
        validateUser(user);
        validateDuplicateUser(user, users);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("При добавлении нового пользователя, у пользователя с ID: {}, присвоено имя согласно его логину: {}",
                    user.getId(), user.getLogin());
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Новый пользователь с ID: {} добавлен ", user.getId());
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("Попытка обновления данных пользователя ID: {}", user.getId());
        User userFromData = users.get(user.getId());
        if (userFromData == null) {
            log.warn("В системе нет пользователя с переданным ID: {}", user.getId());
            throw new ValidationException("Пользователь с ID: " + user.getId() + " не найден");
        }
        validateUser(user);
        validateDuplicateUser(user, users);

        if (user.getName() != null && !user.getName().isBlank()) {
            userFromData.setName(user.getName());
        } else if (userFromData.getName().equals(userFromData.getLogin())) {
            userFromData.setName(user.getLogin());
        }
        userFromData.setBirthday(user.getBirthday());
        userFromData.setLogin(user.getLogin());
        userFromData.setEmail(user.getEmail());
        return userFromData;
    }


    private Integer getNextId() {
        int currentMaxId = users.values().stream()
                .mapToInt(User::getId)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }


}
