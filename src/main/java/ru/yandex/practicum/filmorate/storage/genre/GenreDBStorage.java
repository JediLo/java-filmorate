package ru.yandex.practicum.filmorate.storage.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Component
public class GenreDBStorage implements GenreStorage {

    private final GenreRepository genreRepository;

    public GenreDBStorage(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public Collection<Genre> findAllGenres() {
        return genreRepository.findAllGenre();
    }

    @Override
    public Optional<Genre> getGenreById(int id) {
        return genreRepository.findGenreById(id);
    }

    @Override
    public boolean genresExist(Collection<Genre> genres) {
        return genreRepository.genresExist(genres);
    }
}
