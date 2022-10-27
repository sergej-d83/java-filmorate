package ru.yandex.practicum.filmorate.storage.dao.impl;

import antlr.PreservingFileWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.MpaRating;
import ru.yandex.practicum.filmorate.storage.dao.FilmDao;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component("FilmDaoImpl")
public class FilmDaoImpl implements FilmDao {

    private final JdbcTemplate jdbcTemplate;

    RowMapper<Film> rowMapper = ((rs, rowNum) -> {
        Film film = new Film();
        MpaRating mpa = new MpaRating();
        mpa.setId(rs.getInt("rating_id"));
        film.setId(rs.getInt("film_id"));
        film.setName(rs.getString("film_name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        film.setMpaRating(mpa);

        return film;
    });

    @Autowired
    public FilmDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film getFilmById(Integer filmId) {
        String sql = "SELECT film_id, film_name, description, release_date, duration, rating_id FROM films WHERE film_id = ?";

        Film film = jdbcTemplate.queryForObject(sql, rowMapper, filmId);
        log.info("Возвращаю фильм: {}", film);

        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT film_id, film_name, description, release_date, duration, rating_id FROM films";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Film createFilm(Film film) {
        String sql = "INSERT INTO films(film_name, description, release_date, duration, rating_id VALUES(?, ?, ?, ?, ?)";
        int insert = jdbcTemplate.update(sql, film.getName(), film.getDescription(),
                            film.getReleaseDate(), film.getDuration(),
                            film.getMpaRating());

        if (insert == 1) {
            log.info("В базе создан новый фильм: {}", film.getName());
        }

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "UPDATE films SET film_name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? "
                   + "WHERE film_id = ?";

        int update = jdbcTemplate.update(sql, film.getName(), film.getDescription(), Date.valueOf(film.getReleaseDate())
                                            , film.getDuration(), film.getMpaRating().getId(), film.getId()
        );
        if (update == 1) {
            log.info("Фильм обновлён: {}", film.getName());
        }

        return film;
    }

    @Override
    public void deleteFilm(Integer id) {
        jdbcTemplate.update("DELETE FROM films WHERE film_id = ?", id);
    }

    @Override
    public boolean isFilmPresent(Integer filmId) {
        try {
            getFilmById(filmId);
            return true;
        } catch (DataAccessException ex) {
            return false;
        }
    }
}
