package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.validations.FilmValidation.validateDuplicatedFilm;
import static ru.yandex.practicum.filmorate.validations.FilmValidation.validateFilm;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAllFilms() {
        log.info("Запрос на получение всех фильмов");
        return films.values();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.info("Попытка добавления фильма {}", film.getName());
        validateDuplicatedFilm(film, films);
        validateFilm(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм с ID: {}, был успешно добавлен", film.getId());
        return film;

    }


    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Попытка изменения фильма с ID: {}", film.getId());
        Film filmFromData = films.get(film.getId());
        if (filmFromData == null) {
            log.warn("В системе нет фильма с переданным ID: {}", film.getId());
            throw new ValidationException("Фильм с таким ID не найден");
        }
        validateDuplicatedFilm(film, films);
        validateFilm(film);
        filmFromData.setName(film.getName());
        filmFromData.setDescription(film.getDescription());
        filmFromData.setDuration(film.getDuration());
        filmFromData.setReleaseDate(film.getReleaseDate());
        log.info("Фильм с ID: {}, был успешно изменен", film.getId());
        return filmFromData;
    }

    private Integer getNextId() {
        int currentMaxId = films.values().stream()
                .mapToInt(Film::getId)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }


}
