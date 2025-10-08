package ru.yandex.practicum.filmorate.service.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaFilm;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;


@Service
@Slf4j
public class MpaService {

    MpaStorage mpaStorage;

    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public MpaFilm findById(int id) {
        return mpaStorage.findById(id).orElseThrow(() -> new NotFoundException("Рейтинга с ID" + id + " не найдено"));
    }

    public List<MpaFilm> findAll() {
        return mpaStorage.findAll();
    }
}
