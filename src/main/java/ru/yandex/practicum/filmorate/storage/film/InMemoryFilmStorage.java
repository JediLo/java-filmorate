package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Collection<Film> findAllFilms() {
        log.debug("Получение всех фильмов");
        return films.values();
    }

    @Override
    public Film addFilm(Film film) {
        films.put(film.getId(), film);
        log.debug("Фильм с ID: {}, был успешно добавлен", film.getId());
        return films.get(film.getId());
    }


    @Override
    public Film getFilmByID(int id) {
        return films.get(id);
    }

    @Override
    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        log.debug("Фильм с ID: {}, был успешно изменен", film.getId());
        return films.get(film.getId());
    }
}
