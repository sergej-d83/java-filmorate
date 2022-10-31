package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.film.Genre;

import java.util.Collection;
import java.util.Set;

public interface GenreDao {
    Genre getGenre(Integer genreId);
    Collection<Genre> getAllGenres();
    Set<Genre> getGenresOfFilm(Integer filmId);
    void addGenreToFilm(Integer filmId, Set<Genre> genres);
    void deleteGenre(Integer filmId);
    void updateFilmGenre(Integer filmId, Set<Genre> genres);
    boolean isGenrePresent(Integer genreId);

}
