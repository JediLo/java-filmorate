package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Collection<User> findAllUsers();

    User addUser(User user);

    User updateUser(User user);

    Optional<User> getUserById(int id);

    void addFriend(int id, int idFriend);

    void removeFriend(int id, int friendId);

    List<User> findAllFriends(int id);

    List<User> findAllMutualFriends(int id, int otherId);
}
