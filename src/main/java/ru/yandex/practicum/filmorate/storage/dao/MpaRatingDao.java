package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.film.MpaRating;

import java.util.Collection;

public interface MpaRatingDao {
    MpaRating getRating(Integer mpaId);
    Collection<MpaRating> getAllRatings();
    boolean isRatingPresent(Integer ratingId);
}
