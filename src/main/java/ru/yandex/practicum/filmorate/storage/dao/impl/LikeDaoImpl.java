package ru.yandex.practicum.filmorate.storage.dao.impl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.MpaRating;
import ru.yandex.practicum.filmorate.storage.dao.LikeDao;

import java.util.Collection;

@Slf4j
@Component
@Data
public class LikeDaoImpl implements LikeDao {

    private final JdbcTemplate jdbcTemplate;
    RowMapper<Film> filmMapper = ((rs, rowNum) -> {
        Film film = new Film();
        MpaRating mpa = new MpaRating();
        mpa.setId(rs.getInt("rating_id"));
        film.setId(rs.getInt("film_id"));
        film.setName(rs.getString("film_name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        film.setMpa(mpa);

        return film;
    });

    @Override
    public void createLike(Integer userId, Integer filmId) {
        String sql = "INSERT INTO film_likes (film_id, user_id) VALUES (?, ?)";
        int insert = jdbcTemplate.update(sql, filmId, userId);

        if (insert == 1) {
            log.info("Пользователь с ID: {} поставил лайк фильму с ID: {}", userId, filmId);
        }
    }

    @Override
    public Collection<Film> getPopularFilms(Integer count) {
        String sql = "SELECT * FROM films "
                + "LEFT JOIN (SELECT film_id, COUNT(DISTINCT user_id) AS likes "
                + "FROM film_likes GROUP BY film_id) AS popular ON films.film_id = popular.film_id "
                + "ORDER BY popular.likes DESC LIMIT ?";
        return jdbcTemplate.query(sql, filmMapper, count);
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
