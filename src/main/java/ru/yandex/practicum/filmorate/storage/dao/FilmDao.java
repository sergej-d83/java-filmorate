package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.Collection;

public interface FilmDao {
    Film getFilmById(Integer id);

    Collection<Film> getAllFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    boolean isFilmPresent(Integer filmId);
}
