package ru.yandex.practicum.filmorate.storage.mpa;


import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.model.MpaFilm;

import java.util.List;
import java.util.Optional;

@Component
public class MpaDBStorage implements MpaStorage {

    private final MpaRepository mpaRepository;

    public MpaDBStorage(MpaRepository mpaRepository) {
        this.mpaRepository = mpaRepository;
    }

    @Override
    public Optional<MpaFilm> findById(int id) {
        return mpaRepository.findMpaById(id);
    }

    @Override
    public List<MpaFilm> findAll() {
        return mpaRepository.findAllMpa();
    }

    @Override
    public boolean mpaExist(int mpaId) {
        return mpaRepository.mpaExist(mpaId);
    }
}
