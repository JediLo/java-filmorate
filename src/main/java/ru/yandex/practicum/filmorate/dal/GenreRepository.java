package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepository extends BaseRepository<Genre> {

    private static final String FIND_ALL_GENRE = "SELECT * FROM genres";
    private static final String FIND_BY_ID = "SELECT * FROM genres WHERE id = ?";

    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public List<Genre> findAllGenre() {
        return findMany(FIND_ALL_GENRE);
    }

    public Optional<Genre> findGenreById(int id) {
        return findOne(FIND_BY_ID, id);
    }

    public boolean genresExist(Collection<Genre> genres) {
        if (genres == null) {
            return true;
        } else {
            return genres.stream().allMatch(genre -> findOne(FIND_BY_ID, genre.getId()).isPresent());
        }
    }
}
