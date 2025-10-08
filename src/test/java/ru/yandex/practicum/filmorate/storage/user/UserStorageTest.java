package ru.yandex.practicum.filmorate.storage.user;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dal.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@JdbcTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, UserRepository.class, UserRowMapper.class})
class UserStorageTest {
    private final UserDbStorage userStorage;

    @Test
    public void testAddUser() {
        User userAdd = new User(0, "email@mail.ru", "login", "name",
                LocalDate.of(2000, 2, 2));
        Optional<User> userOptional = userStorage.getUserById(1);
        assertThat(userOptional).isPresent();
    }

    @Test
    public void testFindUserByIdWhenCleanDB() {
        Optional<User> userOptional = userStorage.getUserById(1);
        assertThat(userOptional).isEmpty();
    }

    @Test
    public void testFindUserById() {
        User userAdd = new User(0, "email@mail.ru", "login", "name",
                LocalDate.of(2000, 2, 2));
        userStorage.addUser(userAdd);
        Optional<User> userOptional = userStorage.getUserById(1);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testFindAllUsers() {
        User userAdd1 = new User(0, "email1@mail.ru", "login1", "name",
                LocalDate.of(2000, 2, 2));
        userStorage.addUser(userAdd1);
        User userAdd2 = new User(0, "email2@mail.ru", "login2", "name",
                LocalDate.of(2000, 2, 2));
        userStorage.addUser(userAdd2);
        User userAdd3 = new User(0, "email3@mail.ru", "login3", "name",
                LocalDate.of(2000, 2, 2));
        userStorage.addUser(userAdd3);
        List<User> users = userStorage.findAllUsers().stream().toList();
        assertThat((users.size())).isEqualTo(3);
    }

    @Test
    public void testUpdateUser() {
        User userAdd = new User(0, "email@mail.ru", "login", "name",
                LocalDate.of(2000, 2, 2));
        userStorage.addUser(userAdd);
        User userUpdate = new User(1, "newEmail@mail.ru", "newLogin", "newName",
                LocalDate.of(1991, 3, 1));
        userStorage.updateUser(userUpdate);
        Optional<User> optUserUpdated = userStorage.getUserById(1);
        User userUpdated = null;
        if (optUserUpdated.isPresent()) {
            userUpdated = optUserUpdated.get();
        }
        assertNotNull(userUpdated);
        assertEquals(1, userUpdated.getId());
        assertEquals(userUpdate.getEmail(), userUpdated.getEmail());
        assertEquals(userUpdate.getLogin(), userUpdated.getLogin());
        assertEquals(userUpdate.getName(), userUpdated.getName());
        assertEquals(userUpdate.getBirthday(), userUpdated.getBirthday());

    }

    @Test
    public void testFriends() {
        // Добавим пользователей в базу
        User userAdd1 = new User(1, "email1@mail.ru", "login1", "name",
                LocalDate.of(2000, 2, 2));
        userStorage.addUser(userAdd1);
        User userAdd2 = new User(2, "email2@mail.ru", "login2", "name",
                LocalDate.of(2000, 2, 2));
        userStorage.addUser(userAdd2);
        User userAdd3 = new User(3, "email3@mail.ru", "login3", "name",
                LocalDate.of(2000, 2, 2));
        userStorage.addUser(userAdd3);
        User userAdd4 = new User(4, "email4@mail.ru", "login4", "name",
                LocalDate.of(2000, 2, 2));
        userStorage.addUser(userAdd4);
        // теперь добавим друзей
        userStorage.addFriend(1, 2);
        userStorage.addFriend(2, 1);
        userStorage.addFriend(2, 3);
        userStorage.addFriend(3, 1);
        userStorage.addFriend(3, 2);
        userStorage.addFriend(3, 4);

        List<User> friends = userStorage.findAllFriends(1);
        assertEquals(1, friends.size());
        friends = userStorage.findAllFriends(2);
        assertEquals(2, friends.size());
        friends = userStorage.findAllFriends(3);
        assertEquals(3, friends.size());

    }

    @Test
    void testFindAllMutualFriends() {
        // Добавим пользователей в базу
        User userAdd1 = new User(1, "email1@mail.ru", "login1", "name",
                LocalDate.of(2000, 2, 2));
        userStorage.addUser(userAdd1);
        User userAdd2 = new User(2, "email2@mail.ru", "login2", "name",
                LocalDate.of(2000, 2, 2));
        userStorage.addUser(userAdd2);
        User userAdd3 = new User(3, "email3@mail.ru", "login3", "name",
                LocalDate.of(2000, 2, 2));
        userStorage.addUser(userAdd3);
        User userAdd4 = new User(4, "email4@mail.ru", "login4", "name",
                LocalDate.of(2000, 2, 2));
        userStorage.addUser(userAdd4);
        // Выстроим связи дружбы
        userStorage.addFriend(1, 2);
        userStorage.addFriend(1, 3);
        userStorage.addFriend(1, 4);
        userStorage.addFriend(2, 3);
        userStorage.addFriend(3, 2);
        userStorage.addFriend(3, 4);
        List<User> mutualFriends = userStorage.findAllMutualFriends(1, 2);
        assertEquals(1, mutualFriends.size());
        mutualFriends = userStorage.findAllMutualFriends(1, 3);
        assertEquals(2, mutualFriends.size());
        // и наоборот
        mutualFriends = userStorage.findAllMutualFriends(2, 1);
        assertEquals(1, mutualFriends.size());
        mutualFriends = userStorage.findAllMutualFriends(3, 1);
        assertEquals(2, mutualFriends.size());

    }
}