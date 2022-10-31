package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.film.MpaRating;
import ru.yandex.practicum.filmorate.storage.dao.MpaRatingDao;

import java.util.Collection;

@Slf4j
@Service
public class MpaRatingService {
    private final MpaRatingDao mpaRatingDao;

    @Autowired
    public MpaRatingService(MpaRatingDao mpaRatingDao) {
        this.mpaRatingDao = mpaRatingDao;
    }

    public Collection<MpaRating> getAll() {
        return mpaRatingDao.getAllRatings();
    }

    public MpaRating getRatingById(Integer ratingId) {
        if (!mpaRatingDao.isRatingPresent(ratingId)) {
            log.info("Рейтинг с ID: {} не найден", ratingId);
            throw new NotFoundException("Рейтинг с ID: " + ratingId + " не найден");
        }
        return mpaRatingDao.getRating(ratingId);
    }

}
