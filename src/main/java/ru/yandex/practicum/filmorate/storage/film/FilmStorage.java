package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {

    Collection<Film> findAllFilms();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Optional<Film> getFilmByID(int id);

    void addLike(int filmId, int userId);

    void removeLike(int filmId, int userId);

    Collection<Film> findPopularFilms(int count);
}
