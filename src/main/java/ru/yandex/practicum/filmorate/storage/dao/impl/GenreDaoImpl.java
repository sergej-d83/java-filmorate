package ru.yandex.practicum.filmorate.storage.dao.impl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@Data
public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Genre> genreMapper = ((rs, rowNum) -> {
        Genre genre = new Genre();
        genre.setId(rs.getInt("genre_id"));
        genre.setName(rs.getString("genre_name"));
        return genre;
    });

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

        List<Genre> genresList = genres.stream().distinct().collect(Collectors.toList());

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, filmId);
                ps.setInt(2, genresList.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return genresList.size();
            }
        });
    }

    @Override
    public void deleteGenre(Integer filmId) {
        String sql = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);
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
