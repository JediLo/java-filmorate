package ru.yandex.practicum.filmorate.dal.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MpaFilm;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MpaRowMapper implements RowMapper<MpaFilm> {

    @Override
    public MpaFilm mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new MpaFilm(rs.getString("name"), rs.getInt("id"));
    }
}
