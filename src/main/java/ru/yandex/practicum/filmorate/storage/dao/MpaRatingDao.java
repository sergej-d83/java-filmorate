package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.film.MpaRating;

import java.util.List;

public interface MpaRatingDao {
    MpaRating getRating(Integer mpaId);
    List<MpaRating> getAllRatings();
    boolean isRatingPresent(Integer ratingId);
}
