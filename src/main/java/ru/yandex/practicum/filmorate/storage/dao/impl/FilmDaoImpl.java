package ru.yandex.practicum.filmorate.storage.dao.impl;

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
import java.util.Collection;


@Slf4j
@Component("FilmDaoImpl")
public class FilmDaoImpl implements FilmDao {

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

    @Autowired
    public FilmDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film getFilmById(Integer filmId) {
        String sql = "SELECT * FROM films WHERE film_id = ?";

        Film film = jdbcTemplate.queryForObject(sql, filmMapper, filmId);
        log.info("Возвращаю фильм: {}", film);

        return film;
    }

    @Override
    public Collection<Film> getAllFilms() {
        String sql = "SELECT * FROM films";
        return jdbcTemplate.query(sql, filmMapper);
    }

    @Override
    public Film createFilm(Film film) {
        String sql = "INSERT INTO films(film_name, description, release_date, duration, rating_id) VALUES(?, ?, ?, ?, ?)";
        int insert = jdbcTemplate.update(sql, film.getName(), film.getDescription(),
                            film.getReleaseDate(), film.getDuration(),
                            film.getMpa().getId());

        if (insert == 1) {
            log.info("В базе создан новый фильм: {}", film);
        } else {
            log.info("Фильм не может быть создан в базе. {}", film);
        }

        String sqlGetFilm = "SELECT * FROM films WHERE film_name = ? AND description = ? AND release_date = ? "
                          + "AND duration = ? AND rating_id = ?";

        return jdbcTemplate.queryForObject(sqlGetFilm, filmMapper, film.getName(), film.getDescription(),
                                                                  film.getReleaseDate(), film.getDuration(),
                                                                  film.getMpa().getId()
        );
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "UPDATE films SET film_name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? "
                   + "WHERE film_id = ?";

        int update = jdbcTemplate.update(sql, film.getName(), film.getDescription(), Date.valueOf(film.getReleaseDate())
                                            , film.getDuration(), film.getMpa().getId(), film.getId()
        );
        if (update == 1) {
            log.info("Фильм обновлён: {}", film.getName());
        }

        return film;
    }

    @Override
    public void deleteFilm(Integer filmId) {
        jdbcTemplate.update("DELETE FROM films WHERE film_id = ?", filmId);
        log.info("Фильм с ID: {} удалён", filmId);
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
