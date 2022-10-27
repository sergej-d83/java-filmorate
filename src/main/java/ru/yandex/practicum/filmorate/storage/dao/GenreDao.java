package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.film.Genre;

import java.util.List;
import java.util.Set;

public interface GenreDao {
    Genre getGenre(Integer genreId);
    List<Genre> getAllGenres();
    void addGenreToFilm(Integer filmId, Set<Genre> genres);
    void updateFilmGenre(Integer filmId, Set<Genre> genres);
    boolean isGenrePresent(Integer genreId);

}
