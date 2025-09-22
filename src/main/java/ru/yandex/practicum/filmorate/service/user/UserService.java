package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
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
        user.setId(getNextId());
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
        User userFromData = userStorage.getUserById(id);
        if (userFromData == null) {
            log.warn("В системе нет пользователя с переданным ID: {}", id);
            throw new NotFoundException("Пользователь с ID: " + id + " не найден");
        }
        log.info("Пользователь с ID: {} получен", userFromData.getId());
        return userFromData;
    }

    private User updateUserData(User user) {
        log.info("Попытка обновить данные пользователя");
        User userFromMemory = getExistingUserById(user.getId());
        String oldName = userFromMemory.getName();
        String oldLogin = userFromMemory.getLogin();
        String newLogin = user.getLogin();
        String newName = user.getName();

        if (newName != null && !newName.isBlank()) {
            userFromMemory.setName(newName);
        } else if (oldName.equals(oldLogin)) {
            userFromMemory.setName(newLogin);
        }

        userFromMemory.setBirthday(user.getBirthday());
        userFromMemory.setLogin(user.getLogin());
        userFromMemory.setEmail(user.getEmail());
        log.info("Данные пользователя обновлены");
        return userFromMemory;
    }


    private Integer getNextId() {
        int currentMaxId = userStorage.findAllUsers().stream()
                .mapToInt(User::getId)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void validateDuplicateUser(User user) {
        log.info("Проверка на дублирование пользователя");
        if (userStorage.findAllUsers().stream()
                .anyMatch(userFromData -> (userFromData.equals(user) && userFromData.getId() != user.getId()))) {
            log.warn("Такая почта: {} уже заведена в базу, запрос отклонен", user.getEmail());
            throw new NotFoundException("Пользователь с такой почтой уже есть");
        }
    }

    public User addFriend(int id, int friendId) {
        log.info("Попытка добавления друга с ID: {} пользователю с ID: {}", friendId, id);
        User user = getExistingUserById(id);
        User friend = getExistingUserById(friendId);
        boolean addFromUser = user.addFriend(friend.getId());
        boolean addFromFriend = friend.addFriend(user.getId());
        if (addFromUser && addFromFriend) {
            log.info("Друг с ID: {} был добавлен пользователю с ID: {}", friend, id);
            return user;
        }
        log.warn("Пользователи {} и {} уже друзья", id, friendId);
        throw new DuplicatedDataException("Пользователи уже друзья");


    }

    public User removeFriend(int id, int friendId) {
        log.info("Попытка удаления друга с ID: {} пользователю с ID: {}", friendId, id);
        User user = getExistingUserById(id);
        User friend = getExistingUserById(friendId);
        user.removeFriend(friend.getId());
        friend.removeFriend(user.getId());
        return user;
    }

    public Collection<User> findAllFriends(int id) {
        log.info("Попытка получения всех друзей пользователя c ID: {}", id);
        User user = getExistingUserById(id);
        return user.getFriendSet().stream()
                .map(userStorage::getUserById)
                .filter(Objects::nonNull)
                .toList();
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
                .filter(Objects::nonNull)
                .toList();

    }
}
