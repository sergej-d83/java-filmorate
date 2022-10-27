package ru.yandex.practicum.filmorate.storage.dao;

public interface LikeDao {
    void createLike(Integer userId, Integer filmId);
    int getLikes(Integer filmId);
    void deleteLike(Integer filmId, Integer userId);
}
