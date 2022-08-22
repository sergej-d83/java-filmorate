package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private int filmId = 1;
    private final Map<Integer, Film> films = new HashMap<>();
    private final LocalDate EARLIEST_DATE = LocalDate.of(1895, 12, 28);

    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    public Film createFilm(Film film) {
        validateReleaseDate(film);
        film.setId(filmId++);
        films.put(film.getId(), film);
        return film;
    }

    public Film updateFilm(Film film) {

        if (films.containsKey(film.getId())) {
            validateReleaseDate(film);
            films.put(film.getId(), film);
        } else {
            throw new ValidationException("Фильм с таким номером не существует.");
        }
        return film;
    }

    private void validateReleaseDate(Film film) {
        if (film.getReleaseDate().isBefore(EARLIEST_DATE)) {
            throw new ValidationException("Дата релиза - не раньше 28.12.1895");
        }
    }
}
