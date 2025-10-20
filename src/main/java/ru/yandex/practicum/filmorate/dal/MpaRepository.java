package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MpaFilm;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaRepository extends BaseRepository<MpaFilm> {

    private static final String FIND_ALL_MPA = "SELECT * FROM ratings";
    private static final String FIND_BY_ID = "SELECT * FROM ratings WHERE id = ?";

    public MpaRepository(JdbcTemplate jdbc, RowMapper<MpaFilm> mapper) {
        super(jdbc, mapper);
    }

    public List<MpaFilm> findAllMpa() {
        return findMany(FIND_ALL_MPA);
    }

    public Optional<MpaFilm> findMpaById(int id) {
        return findOne(FIND_BY_ID, id);
    }

    public boolean mpaExist(int ratingId) {
        if (ratingId > 0) {
            return findOne(FIND_BY_ID, ratingId).isPresent();
        } else return false;
    }
}
