package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAllUsers() {

        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getExistingUserById(id);
    }

    @GetMapping("{id}/friends")
    public Collection<User> findAllFriends(@PathVariable int id) {
        return userService.findAllFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public Collection<User> findAllMutualFriends(@PathVariable int id,
                                                 @PathVariable int otherId) {
        return userService.findAllMutualFriends(id, otherId);
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {

        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {

        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable int id,
                          @PathVariable int friendId) {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User removeFriend(@PathVariable int id,
                             @PathVariable int friendId) {
        return userService.removeFriend(id, friendId);
    }

}
