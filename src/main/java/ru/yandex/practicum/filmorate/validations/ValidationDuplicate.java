package ru.yandex.practicum.filmorate.validations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@Slf4j
@Component
public class ValidationDuplicate {

    final UserStorage userStorage;
    final FilmStorage filmStorage;

    public ValidationDuplicate(UserStorage userStorage, FilmStorage filmStorage){
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }




}
