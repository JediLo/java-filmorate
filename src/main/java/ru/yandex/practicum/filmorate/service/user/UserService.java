package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(@Qualifier("bd") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> findAllUsers() {
        log.info("Попытка получения всех пользователей");
        return userStorage.findAllUsers();
    }


    public User addUser(User user) {
        log.info("Попытка добавления пользователя с ID: {}", user.getId());
        validateDuplicateUser(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("При добавлении нового пользователя, у пользователя с ID: {}, присвоено имя согласно его логину: {}",
                    user.getId(), user.getLogin());
        }
        User saved = userStorage.addUser(user);
        log.info("Новый пользователь с ID: {} добавлен ", saved.getId());
        return saved;
    }


    public User updateUser(User user) {
        log.info("Попытка обновления данных пользователя ID: {}", user.getId());
        validateDuplicateUser(user);
        User updated = userStorage.updateUser(updateUserData(user));
        log.info("Пользователь с ID: {}, был успешно обновлен", updated.getId());
        return updated;
    }

    public User getExistingUserById(int id) {
        log.info("Попытка получения пользователя с ID: {}", id);
        Optional<User> userFromData = userStorage.getUserById(id);
        if (userFromData.isEmpty()) {
            log.warn("В системе нет пользователя с переданным ID: {}", id);
            throw new NotFoundException("Пользователь с ID: " + id + " не найден");
        }
        log.info("Пользователь с ID: {} получен", id);
        return userFromData.get();
    }

    private User updateUserData(User user) {
        log.info("Попытка обновить данные пользователя");
        Optional<User> userFromData = userStorage.getUserById(user.getId());
        if (userFromData.isEmpty()) {
            log.warn("Вы пытаетесь обновить данные несуществующего пользователя с ID : {}", user.getId());
            throw new NotFoundException("Пользователь с ID: " + user.getId() + " не найден");
        } else {
            User updatedUser = userStorage.updateUser(user);
            log.info("Данные пользователя обновлены");
            return updatedUser;
        }
    }

    private void validateDuplicateUser(User user) {
        log.info("Проверка на дублирование пользователя");
        if (userStorage.findAllUsers().stream()
                .anyMatch(userFromData -> (userFromData.equals(user) && userFromData.getId() != user.getId()))) {
            log.warn("Такая почта: {} уже заведена в базу, запрос отклонен", user.getEmail());
            throw new NotFoundException("Пользователь с такой почтой уже есть");
        }
    }

    public void addFriend(int id, int friendId) {
        log.info("Попытка добавить друга");
        Optional<User> user = userStorage.getUserById(id);
        Optional<User> userFriend = userStorage.getUserById(friendId);
        if (user.isEmpty() || userFriend.isEmpty()) {
            throw new NotFoundException("При добавлении друга у пользователя " + id + " этот пользователь не найден");
        } else {
            userStorage.addFriend(id, friendId);
        }
    }

    public void removeFriend(int id, int friendId) {
        log.info("Попытка удаления друга с ID: {} пользователю с ID: {}", friendId, id);
        Optional<User> user = userStorage.getUserById(id);
        Optional<User> userFriend = userStorage.getUserById(friendId);
        if (user.isEmpty() || userFriend.isEmpty()) {
            throw new NotFoundException("Пользователь с id " + id + " не найден при удалении друга");
        } else {
            userStorage.removeFriend(id, friendId);
        }

    }

    public Collection<User> findAllFriends(int id) {
        log.info("Попытка получения всех друзей пользователя c ID: {}", id);
        Optional<User> user = userStorage.getUserById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("При запросе всех друзей пользователя " + id + "пользователь не найден");
        } else {
            return userStorage.findAllFriends(id);
        }
    }

    public Collection<User> findAllMutualFriends(int id, int otherId) {
        log.info("Попытка получения всех общих друзей пользователей с ID: {} и {}", id, otherId);
        User user = getExistingUserById(id);
        User other = getExistingUserById(otherId);
        Set<Integer> friendUser = user.getFriendSet();
        Set<Integer> friendOther = other.getFriendSet();

        Set<Integer> mutual = new HashSet<>(friendUser);
        mutual.retainAll(friendOther);

        log.info("Получаем список общих друзей");
        return mutual.stream()
                .map(userStorage::getUserById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

    }
}
