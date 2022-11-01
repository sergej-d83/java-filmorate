package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.storage.dao.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Service("FilmService")
@Slf4j
public class FilmService {

    private final FilmDao filmDao;
    private final UserDao userDao;
    private final GenreDao genreDao;
    private final LikeDao likeDao;
    private final MpaRatingDao mpaRatingDao;
    private final LocalDate EARLIEST_DATE = LocalDate.of(1895, 12, 28);


    @Autowired
    public FilmService(@Qualifier("FilmDaoImpl") FilmDao filmDao,
                       @Qualifier("UserDaoImpl") UserDao userDao, GenreDao genreDao,
                       LikeDao likeDao, MpaRatingDao mpaRatingDao
    ) {
        this.filmDao = filmDao;
        this.userDao = userDao;
        this.genreDao = genreDao;
        this.likeDao = likeDao;
        this.mpaRatingDao = mpaRatingDao;
    }

    public Collection<Film> getAllFilms() {
        List<Film> films = (List<Film>) filmDao.getAllFilms();
        for (Film film : films) {
            film.setGenres(genreDao.getGenresOfFilm(film.getId()));
            film.setMpa(mpaRatingDao.getRating(film.getMpa().getId()));
        }
        return films;
    }

    public Film getFilmById(Integer filmId) {
        if (!filmDao.isFilmPresent(filmId)) {
            throw new NotFoundException("Фильм с этим ID: " + filmId + " не найден");
        }

        Film film = filmDao.getFilmById(filmId);
        film.setGenres(genreDao.getGenresOfFilm(filmId));
        film.setMpa(mpaRatingDao.getRating(film.getMpa().getId()));
        return film;
    }

    public Film createFilm(Film film) {

        if (film.getId() != 0) {
            if (filmDao.isFilmPresent(film.getId())) {
                log.info("Фильм с таким ID: {} уже существует", film.getId());
                throw new AlreadyExistsException("Фильм с таким ID: " + film.getId() + " уже существует:");
            } else {
                throw new IllegalArgumentException("Не правильный идентификатор фильма: " + film.getId());
            }
        }

        validateReleaseDate(film);
        checkFilmData(film);

        Film newFilm = filmDao.createFilm(film);

        genreDao.addGenreToFilm(newFilm.getId(), film.getGenres());
        newFilm.setGenres(genreDao.getGenresOfFilm(newFilm.getId()));
        newFilm.setMpa(mpaRatingDao.getRating(newFilm.getMpa().getId()));

        return newFilm;
    }

    public Film updateFilm(Film film) {

        if (!filmDao.isFilmPresent(film.getId())) {
            log.info("Фильм с таким ID: {} не найден", film.getId());
            throw new NotFoundException("Фильм с таким ID: " + film.getId() + " не найден:");
        }
        validateReleaseDate(film);
        checkFilmData(film);

        Film updatedFilm = filmDao.updateFilm(film);

        genreDao.deleteGenre(updatedFilm.getId());
        genreDao.addGenreToFilm(film.getId(), film.getGenres());
        updatedFilm.setGenres(genreDao.getGenresOfFilm(updatedFilm.getId()));
        updatedFilm.setMpa(mpaRatingDao.getRating(updatedFilm.getMpa().getId()));

        return updatedFilm;
    }

    public void addLike(Integer filmId, Integer userId) {
        likeDao.createLike(userId, filmId);
    }

    public void removeLike(Integer filmId, Integer userId) {
        if (!userDao.isUserPresent(userId)) {
            throw new NotFoundException("User not found: ID " + userId);
        } else if (!filmDao.isFilmPresent(filmId)) {
            throw new NotFoundException("Film not found: ID " + filmId);
        }
        likeDao.deleteLike(filmId, userId);
    }

    public Collection<Film> getMostPopularFilms(int count) {

        return likeDao.getPopularFilms(count);
    }

    private void validateReleaseDate(Film film) {
        if (film.getReleaseDate().isBefore(EARLIEST_DATE)) {
            log.error("Дата релиза - не раньше 28.12.1895");
            throw new ValidationException("Дата релиза - не раньше 28.12.1895");
        }
    }

    private void checkFilmData(Film film) {

        if (mpaRatingDao.isRatingPresent(film.getMpa().getId())) {
            log.info("Рейтинг с ID: {} не найден", film.getMpa().getId());
            throw new NotFoundException("Рейтинг с ID: " + film.getMpa().getId() + " не найден");
        }
        for (Genre genre : film.getGenres()) {
            if (!genreDao.isGenrePresent(genre.getId())) {
                log.info("Жанр с ID: {} не найден", genre.getId());
                throw new NotFoundException("Жанр с ID: " + genre.getId() + " не найден");
            }
        }
    }
}
