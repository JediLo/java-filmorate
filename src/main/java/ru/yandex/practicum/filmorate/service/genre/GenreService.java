package ru.yandex.practicum.filmorate.service.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;

@Service
@Slf4j
public class GenreService {

    private final GenreStorage genreStorage;


    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Collection<Genre> findAllGenre() {
        log.info("Попытка получения всех жанров");
        return genreStorage.findAllGenres();

    }

    public Genre findGenreById(int id) {
        log.info("Попытка получения жанра с ID {}", id);
        return genreStorage.getGenreById(id).orElseThrow(() -> new NotFoundException("Жанр с ID " + id + " не найден"));
    }
}
