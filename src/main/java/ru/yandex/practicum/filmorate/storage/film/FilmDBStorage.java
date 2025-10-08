package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Qualifier("bd")
@Component
public class FilmDBStorage implements FilmStorage {

    private final FilmRepository filmRepository;

    FilmDBStorage(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @Override
    public Collection<Film> findAllFilms() {
        return filmRepository.findAll();
    }

    @Override
    public Film addFilm(Film film) {
        return filmRepository.addFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        return filmRepository.updateFilm(film);
    }

    @Override
    public Optional<Film> getFilmByID(int id) {
        return filmRepository.findById(id);
    }

    @Override
    public void addLike(int filmId, int userId) {
        filmRepository.addLike(filmId, userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        filmRepository.removeLike(filmId, userId);
    }

    @Override
    public Collection<Film> findPopularFilms(int count) {
        return filmRepository.findPopularFilms(count);
    }
}
