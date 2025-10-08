package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Slf4j
@Qualifier("bd")
@Component
public class UserDbStorage implements UserStorage {

    private final UserRepository userRepository;

    public UserDbStorage(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Collection<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User addUser(User user) {
        return userRepository.addUser(user);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.updateUser(user);
    }

    @Override
    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public void addFriend(int id, int idFriend) {
        userRepository.addFriend(id, idFriend);
    }

    @Override
    public void removeFriend(int id, int friendId) {
        userRepository.removeFriend(id, friendId);
    }

    @Override
    public List<User> findAllFriends(int id) {
        return userRepository.findAllFriendsById(id);
    }

    @Override
    public List<User> findAllMutualFriends(int id, int otherId) {
        return userRepository.findAllMutualFriends(id, otherId);
    }
}
