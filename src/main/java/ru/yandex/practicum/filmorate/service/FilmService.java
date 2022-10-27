package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.dao.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<Film> getAllFilms() {
        return filmDao.getAllFilms();
    }

    public Film getFilmById(Integer filmId) {
        return filmDao.getFilmById(filmId);
    }

    public Film createFilm(Film film) {
        validateReleaseDate(film);
        return filmDao.createFilm(film);
    }

    public Film updateFilm(Film film) {
        validateReleaseDate(film);
        return filmDao.updateFilm(film);
    }

    public void addLike(Integer filmId, Integer userId) {
        likeDao.createLike(userId, filmId);
    }

    public void removeLike(Integer filmId, Integer userId) {
       likeDao.deleteLike(filmId, userId);
    }

    public List<Film> getMostPopularFilms(Integer count) {

        return filmDao.getAllFilms().stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    private void validateReleaseDate(Film film) {
        if (film.getReleaseDate().isBefore(EARLIEST_DATE)) {
            log.error("Дата релиза - не раньше 28.12.1895");
            throw new ValidationException("Дата релиза - не раньше 28.12.1895");
        }
    }
}
