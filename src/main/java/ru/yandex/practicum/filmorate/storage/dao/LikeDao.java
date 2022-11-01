package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.Collection;

public interface LikeDao {
    void createLike(Integer userId, Integer filmId);

    Collection<Film> getPopularFilms(Integer count);

    void deleteLike(Integer filmId, Integer userId);
}
