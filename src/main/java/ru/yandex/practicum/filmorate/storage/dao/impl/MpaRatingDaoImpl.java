package ru.yandex.practicum.filmorate.storage.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.MpaRating;
import ru.yandex.practicum.filmorate.storage.dao.MpaRatingDao;

import java.util.List;

@Slf4j
@Component
public class MpaRatingDaoImpl implements MpaRatingDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<MpaRating> ratingMapper = ((rs, rowNum) -> {
        MpaRating mpaRating = new MpaRating();
        mpaRating.setId(rs.getInt("mpa_rating_id"));
        mpaRating.setName(rs.getString("rating_name"));
        return mpaRating;
    });

    @Autowired
    public MpaRatingDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MpaRating getRating(Integer mpaId) {
        String sql = "SELECT mpa_rating_id, rating_name FROM mpa_ratings WHERE mpa_rating_id = ?";

        MpaRating mpaRating = jdbcTemplate.queryForObject(sql, ratingMapper, mpaId);
        log.info("Возвращаю рейтинг фильма: {}", mpaRating);

        return mpaRating;
    }

    @Override
    public List<MpaRating> getAllRatings() {
        String sql = "SELECT mpa_rating_id, rating_name FROM mpa_ratings ORDER BY mpa_rating_id";

        List<MpaRating> ratings = jdbcTemplate.query(sql, ratingMapper);

        log.info("Возвращаю все рейтинги: {}", ratings);
        return ratings;
    }

    @Override
    public boolean isRatingPresent(Integer ratingId) {
        try {
            getRating(ratingId);
            return true;
        } catch (DataAccessException ex) {
            return false;
        }
    }
}
