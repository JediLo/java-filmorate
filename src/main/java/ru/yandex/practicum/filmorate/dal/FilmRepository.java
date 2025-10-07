package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.dal.mapper.ValIntegerRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaFilm;

import java.util.*;

@Repository
public class FilmRepository extends BaseRepository<Film> {

    private static final String FIND_ALL_FILMS = "SELECT f.*, r.name AS rating " +
            "FROM films f " +
            "LEFT JOIN ratings r ON f.rating_id = r.id ";
    private static final String FIND_ALL_GENRE_BY_FILM_ID = "SELECT g.* " +
            "FROM film_genres fg " +
            "LEFT JOIN genres g ON fg.genre_id = g.id " +
            "WHERE fg.film_id = ?";
    private static final String FIND_FILM_BY_ID = "SELECT * , r.name AS rating " +
            "FROM films f " +
            "LEFT JOIN ratings r ON f.rating_id = r.id " +
            "WHERE f.id = ?";
    private static final String INSERT_FILM_QUERY = "INSERT INTO films(name, description,release_date,duration) " +
            "VALUES (?, ?, ?, ?)";
    //private static final String FIND_RATING_BY_ID = "SELECT id AS val FROM ratings WHERE id = ?";
    private static final String UPDATE_FILM_RATING = "UPDATE films SET rating_id = ? WHERE id = ? ";
    private static final String INSERT_FILM_GENRE = "INSERT INTO film_genres(film_id, genre_id) VALUES (?, ?)";
    //private static final String FIND_GENRE_BY_ID = "SELECT id AS val FROM genres WHERE id = ?";
    private static final String UPDATE_FILM_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ? " +
            "WHERE id = ?;";
    private static final String INSERT_LIKE = "INSERT INTO likes_films (user_id, film_id) " +
            "VALUES (?, ?);";
    private static final String DELETE_LIKE = "DELETE FROM likes_films WHERE user_id = ? AND film_id = ?;";
    private static final String FIND_ALL_LIKE = "SELECT user_id AS val FROM likes_films WHERE film_id = ?";


    private final GenreRowMapper genreRowMapper;
    private final ValIntegerRowMapper valIntegerRowMapper;

    public FilmRepository(JdbcTemplate jdbc,
                          GenreRowMapper genreRowMapper,
                          ValIntegerRowMapper valIntegerRowMapper,
                          RowMapper<Film> mapper) {
        super(jdbc, mapper);
        this.genreRowMapper = genreRowMapper;
        this.valIntegerRowMapper = valIntegerRowMapper;
    }

    public List<Film> findAll() {
        List<Film> films = findMany(FIND_ALL_FILMS);
        addAllGenresAndLikes(films);
        return films;
    }

    public Optional<Film> findById(int id) {
        Optional<Film> film = findOne(FIND_FILM_BY_ID, id);
        film.ifPresent(this::addGenres);
        film.ifPresent(this::addLikes);
        return film;
    }

    public Film addFilm(Film film) {
        System.out.println(film);
        int id = insert(INSERT_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration());
        film.setId(id);

        addGenresAndRatingToDB(film);

        return film;
    }

    private void addLikes(Film film) {
        List<Integer> likes = queryMany(FIND_ALL_LIKE, valIntegerRowMapper, film.getId());
        if (likes != null) {
            film.setLikesUsers(new HashSet<>(likes));
        }
    }


    public Film updateFilm(Film film) {
        update(UPDATE_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId());
        addGenresAndRatingToDB(film);
        return film;
    }

    public void addLike(int filmId, int userId) {
        insertWithoutKey(INSERT_LIKE,
                userId,
                filmId);
    }

    public void removeLike(int filmId, int userId) {
        delete(DELETE_LIKE,
                userId,
                filmId);
    }

    private void addAllGenresAndLikes(Collection<Film> films) {
        films.forEach(this::addGenres);
        films.forEach(this::addLikes);
    }


    private void addGenres(Film film) {
        List<Genre> genres = queryMany(FIND_ALL_GENRE_BY_FILM_ID, genreRowMapper, film.getId());

        film.setGenres(new HashSet<>(genres));
    }

    private void addGenresAndRatingToDB(Film film) {
        MpaFilm mpaFilm = film.getMpa();

        if (mpaFilm != null) {
            insert(UPDATE_FILM_RATING,
                    mpaFilm.getId(),
                    film.getId());
        }
        Set<Genre> filmGenders = film.getGenres();
        if (filmGenders != null) {
            filmGenders.forEach(genre -> insert(INSERT_FILM_GENRE,
                    film.getId(),
                    genre.getId()));
        }
    }


}
