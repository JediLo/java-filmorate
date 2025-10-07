package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.MpaFilm;

import java.util.List;
import java.util.Optional;

public interface MpaStorage {

    Optional<MpaFilm> findById(int id);

    List<MpaFilm> findAll();

    boolean mpaExist(int mpaId);
}
