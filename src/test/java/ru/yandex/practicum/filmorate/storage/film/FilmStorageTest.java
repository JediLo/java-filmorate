package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dal.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.dal.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaFilm;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDBStorage.class, FilmRepository.class, FilmRowMapper.class, MpaFilm.class,
        Genre.class, GenreRowMapper.class, UserDbStorage.class, UserRepository.class, UserRowMapper.class})
class FilmStorageTest {

    private final FilmDBStorage filmStorage;
    private final UserDbStorage userStorage;

    @Test
    void testFindAllFilms() {
        Film film1 = new Film(1, "name1", "description",
                LocalDate.of(2000, 2, 2), 200,
                new MpaFilm("Комедия", 1), Set.of(new Genre("P-13", 3), new Genre("R", 4)));
        Film film2 = new Film(2, "name2", "description",
                LocalDate.of(2000, 2, 2), 200,
                new MpaFilm("Комедия", 1), Set.of(new Genre("P-13", 3), new Genre("R", 4)));
        Film film3 = new Film(3, "name3", "description",
                LocalDate.of(2000, 2, 2), 200,
                new MpaFilm("Комедия", 1), Set.of(new Genre("P-13", 3), new Genre("R", 4)));

        filmStorage.addFilm(film1);
        filmStorage.addFilm(film2);
        filmStorage.addFilm(film3);

        List<Film> films = filmStorage.findAllFilms().stream().toList();
        assertEquals(3, films.size());
    }

    @Test
    void testAddFilm() {
        Film film1 = new Film(0, "name1", "description",
                LocalDate.of(2000, 2, 2), 200,
                new MpaFilm("Комедия", 1), Set.of(new Genre("P-13", 3), new Genre("R", 4)));
        filmStorage.addFilm(film1);
        Optional<Film> filmOptional = filmStorage.getFilmByID(1);
        assertThat(filmOptional).isPresent();
    }

    @Test
    void teatUpdateFilm() {
        Film film1 = new Film(1, "name1", "description",
                LocalDate.of(2000, 2, 2), 200,
                new MpaFilm("Комедия", 1), Set.of(new Genre("P-13", 3), new Genre("R", 4)));
        filmStorage.addFilm(film1);
        Film film2 = new Film(1, "nameNew", "descriptionNew",
                LocalDate.of(2003, 3, 3), 201,
                new MpaFilm("Комедия", 2), Set.of(new Genre("P-13", 4)));
        filmStorage.updateFilm(film2);
        Optional<Film> optFilm = filmStorage.getFilmByID(1);
        assertThat(optFilm).isPresent();
        Film film = null;
        if (optFilm.isPresent()) {
            film = optFilm.get();
        }
        Assertions.assertNotNull(film);
        assertEquals(1, film.getId());
        assertEquals("nameNew", film.getName());
        assertEquals("descriptionNew", film.getDescription());
        assertEquals(LocalDate.of(2003, 3, 3), film.getReleaseDate());
        assertEquals(201, film.getDuration());
        assertEquals(2, film.getMpa().getId());
        assertEquals(4, film.getGenres().stream().findFirst().get().getId());
        assertEquals(1, film.getGenres().size());

    }

    @Test
    void testGetFilmByID() {
        Film film1 = new Film(0, "name1", "description",
                LocalDate.of(2000, 2, 2), 200,
                new MpaFilm("Комедия", 1), Set.of(new Genre("P-13", 3), new Genre("R", 4)));
        filmStorage.addFilm(film1);
        Optional<Film> film = filmStorage.getFilmByID(1);
        assertThat(film).isPresent();
    }

    @Test
    void testAddLike() {
        Film film1 = new Film(1, "name1", "description",
                LocalDate.of(2000, 2, 2), 200,
                new MpaFilm("Комедия", 1), Set.of(new Genre("P-13", 3), new Genre("R", 4)));
        Film film2 = new Film(2, "name2", "description",
                LocalDate.of(2000, 2, 2), 200,
                new MpaFilm("Комедия", 1), Set.of(new Genre("P-13", 3), new Genre("R", 4)));
        Film film3 = new Film(3, "name3", "description",
                LocalDate.of(2000, 2, 2), 200,
                new MpaFilm("Комедия", 1), Set.of(new Genre("P-13", 3), new Genre("R", 4)));
        User userAdd1 = new User(1, "email1@mail.ru", "login1", "name",
                LocalDate.of(2000, 2, 2));
        userStorage.addUser(userAdd1);
        User userAdd2 = new User(2, "email2@mail.ru", "login2", "name",
                LocalDate.of(2000, 2, 2));
        userStorage.addUser(userAdd2);
        User userAdd3 = new User(3, "email3@mail.ru", "login3", "name",
                LocalDate.of(2000, 2, 2));
        userStorage.addUser(userAdd3);
        filmStorage.addFilm(film1);
        filmStorage.addFilm(film2);
        filmStorage.addFilm(film3);

        filmStorage.addLike(film2.getId(), userAdd1.getId());
        filmStorage.addLike(film2.getId(), userAdd2.getId());
        filmStorage.addLike(film2.getId(), userAdd3.getId());

        filmStorage.addLike(film3.getId(), userAdd2.getId());
        filmStorage.addLike(film3.getId(), userAdd3.getId());

        filmStorage.addLike(film1.getId(), userAdd3.getId());

        Collection<Film> films = filmStorage.findPopularFilms(3);

        assertEquals(3, films.size());
        List<Film> popular = new ArrayList<>(films);

        // Проверяем порядок по убыванию лайков
        assertEquals(film2.getId(), popular.get(0).getId());
        assertEquals(film3.getId(), popular.get(1).getId());
        assertEquals(film1.getId(), popular.get(2).getId());
    }

    @Test
    void testRemoveLike() {

        Film film2 = new Film(1, "name2", "description",
                LocalDate.of(2000, 2, 2), 200,
                new MpaFilm("Комедия", 1), Set.of(new Genre("P-13", 3), new Genre("R", 4)));
        Film film3 = new Film(2, "name3", "description",
                LocalDate.of(2000, 2, 2), 200,
                new MpaFilm("Комедия", 1), Set.of(new Genre("P-13", 3), new Genre("R", 4)));
        User userAdd1 = new User(1, "email1@mail.ru", "login1", "name",
                LocalDate.of(2000, 2, 2));
        userStorage.addUser(userAdd1);
        User userAdd2 = new User(2, "email2@mail.ru", "login2", "name",
                LocalDate.of(2000, 2, 2));
        userStorage.addUser(userAdd2);
        User userAdd3 = new User(3, "email3@mail.ru", "login3", "name",
                LocalDate.of(2000, 2, 2));
        userStorage.addUser(userAdd3);

        filmStorage.addFilm(film2);
        filmStorage.addFilm(film3);

        filmStorage.addLike(film2.getId(), userAdd1.getId());
        filmStorage.addLike(film2.getId(), userAdd2.getId());
        filmStorage.addLike(film2.getId(), userAdd3.getId());

        filmStorage.addLike(film3.getId(), userAdd2.getId());
        filmStorage.addLike(film3.getId(), userAdd3.getId());

        Collection<Film> films = filmStorage.findPopularFilms(2);

        List<Film> popular = new ArrayList<>(films);
        // Проверяем порядок по убыванию лайков
        assertEquals(film2.getId(), popular.get(0).getId());
        assertEquals(film3.getId(), popular.get(1).getId());
        filmStorage.removeLike(film2.getId(), userAdd1.getId());
        filmStorage.removeLike(film2.getId(), userAdd2.getId());
        films = filmStorage.findPopularFilms(2);


        popular = new ArrayList<>(films);
        assertEquals(film3.getId(), popular.get(0).getId());
        assertEquals(film2.getId(), popular.get(1).getId());

    }

}