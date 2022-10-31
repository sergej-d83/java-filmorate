package ru.yandex.practicum.filmorate.storage.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Genre> genreMapper = ((rs, rowNum) -> {
        Genre genre = new Genre();
        genre.setId(rs.getInt("genre_id"));
        genre.setName(rs.getString("genre_name"));
        return genre;
    });

    @Autowired
    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenre(Integer genreId) {
        String sql = "SELECT genre_id, genre_name FROM genres WHERE genre_id = ?";

        Genre genre = jdbcTemplate.queryForObject(sql, genreMapper, genreId);
        log.info("Возвращаю жанр: {}", genre);

        return genre;
    }

    @Override
    public Collection<Genre> getAllGenres() {
        String sql = "SELECT genre_id, genre_name FROM genres ORDER BY genre_id";
        List<Genre> genres = jdbcTemplate.query(sql, genreMapper);
        log.info("Возвращаю все жанры: {}", genres);
        return genres;
    }

    @Override
    public Set<Genre> getGenresOfFilm(Integer filmId) {
        String sql = "SELECT * FROM film_genres AS fg "
                    + "LEFT JOIN genres AS g ON g.genre_id = fg.genre_id "
                    + "WHERE fg.film_id = ? ORDER BY fg.genre_id";

        return new HashSet<>(jdbcTemplate.query(sql, genreMapper, filmId));
    }

    @Override
    public void addGenreToFilm(Integer filmId, Set<Genre> genres) {
        String sql = "INSERT INTO film_genres (film_id, genre_id) VALUES(?, ?)";

        for (Genre genre : genres) {
            jdbcTemplate.update(sql, filmId, genre.getId());
            log.info("Добавляю фильму с ID: {} жанр: {}", filmId, genre.getName());
        }
    }

    @Override
    public void deleteGenre(Integer filmId) {
        String sql = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);
    }

    @Override
    public void updateFilmGenre(Integer filmId, Set<Genre> genres) {
        String sql = "UPDATE film_genres SET genre_id = ? WHERE film_id = ?";
        for (Genre genre : genres) {
            jdbcTemplate.update(sql, genre.getId(), filmId);
            log.info("Обновляю жанр с ID: {} у фильма с ID: {}", genre.getId(), filmId);
        }
    }

    @Override
    public boolean isGenrePresent(Integer genreId) {
        try {
            getGenre(genreId);
            return true;
        } catch (DataAccessException ex) {
            return false;
        }
    }
}
