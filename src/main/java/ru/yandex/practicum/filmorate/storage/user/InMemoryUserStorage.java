package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;


@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();


    @Override
    public Collection<User> findAllUsers() {
        log.debug("Запрос на получение всех пользователей");
        return users.values();
    }

    @Override
    public User addUser(User user) {
        users.put(user.getId(), user);
        log.debug("Новый пользователь с ID: {} добавлен ", user.getId());
        return users.get(user.getId());
    }

    @Override
    public User updateUser(User user) {
        log.debug("Попытка обновления данных пользователя ID: {}", user.getId());
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public Optional<User> getUserById(int id) {
        return Optional.of(users.get(id));
    }

    @Override
    public void addFriend(int id, int idFriend) {

    }

    @Override
    public void removeFriend(int id, int friendId) {
    }

    @Override
    public List<User> findAllFriends(int id) {
        return List.of();
    }

    @Override
    public List<User> findAllMutualFriends(int id, int otherId) {
        return List.of();
    }


}

