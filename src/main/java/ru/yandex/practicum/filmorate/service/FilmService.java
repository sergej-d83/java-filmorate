package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final LocalDate EARLIEST_DATE = LocalDate.of(1895, 12, 28);


    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Integer filmId) {
        return filmStorage.getFilmById(filmId);
    }

    public Film createFilm(Film film) {
        validateReleaseDate(film);
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        validateReleaseDate(film);
        return filmStorage.updateFilm(film);
    }

    public void addLike(Integer filmId, Integer userId) {
        filmStorage.getFilmById(filmId).getLikes().add(userId);
    }

    public void removeLike(Integer filmId, Integer userId) {
        filmStorage.getFilmById(filmId).getLikes().remove(userId);
    }

    public List<Film> getMostPopularFilms(Integer count) {
        return filmStorage.getAllFilms().stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    private void validateReleaseDate(Film film) {
        if (film.getReleaseDate().isBefore(EARLIEST_DATE)) {
            log.error("Дата релиза - не раньше 28.12.1895");
            throw new ValidationException("Дата релиза - не раньше 28.12.1895");
        }
    }
}
