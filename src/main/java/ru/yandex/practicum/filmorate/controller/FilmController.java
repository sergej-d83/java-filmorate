package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private int filmId = 1;
    private final Map<Integer, Film> films = new HashMap<>();
    private final LocalDate EARLIEST_DATE = LocalDate.of(1895, 12, 28);

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Запрос всех фильмов.");
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        validateReleaseDate(film);
        film.setId(filmId++);
        films.put(film.getId(), film);
        log.info("Добавлен новый фильм: {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {

        if (films.containsKey(film.getId())) {
            validateReleaseDate(film);
            films.put(film.getId(), film);
            log.info("Фильм обновлен: {}", film);
        } else {
            log.error("Фильм с таким номером не существует. ID: {}", film.getId());
            throw new ValidationException("Фильм с таким номером не существует.");
        }
        return film;
    }

    private void validateReleaseDate(Film film) {

        if (film.getReleaseDate().isBefore(EARLIEST_DATE)) {
            log.error("Дата релиза - не раньше 28.12.1895");
            throw new ValidationException("Дата релиза - не раньше 28.12.1895");
        }
    }
}
