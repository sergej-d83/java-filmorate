package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film getFilmById(Integer id);
    List<Film> getAllFilms();
    void createFilm(Film film);
    void updateFilm(Film film);


}
