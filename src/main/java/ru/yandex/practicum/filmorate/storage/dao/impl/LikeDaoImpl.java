package ru.yandex.practicum.filmorate.storage.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.dao.LikeDao;

import java.util.Objects;

@Slf4j
@Component
public class LikeDaoImpl implements LikeDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createLike(Integer userId, Integer filmId) {
        String sql = "INSERT INTO film_likes (film_id, user_id) VALUES (?, ?)";
        int insert = jdbcTemplate.update(sql, filmId, userId);

        if (insert == 1) {
            log.info("Пользователь ID: {} поставил лайк фильму ID: {}", userId, filmId);
        }
    }

    @Override
    public int getLikes(Integer filmId) {
        String sql = "SELECT COUNT(*) FROM film_likes WHERE film_id = ?";
        Integer likes = Objects.requireNonNull(jdbcTemplate.queryForObject(sql, Integer.class, filmId));
        log.info("У фильма ID: {}, {} лайков.", filmId, likes);
        return likes;
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        String sql = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";
        int delete = jdbcTemplate.update(sql, filmId, userId);
        if (delete == 1) {
            log.info("У фильма ID: {} удалён лайк от пользователя ID: {}", filmId, userId);
        }
    }
}
