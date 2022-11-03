package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.film.MpaRating;
import ru.yandex.practicum.filmorate.service.MpaRatingService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@Data
public class MpaRatingController {
    private final MpaRatingService mpaRatingService;

    @GetMapping
    public Collection<MpaRating> getAllMpaRatings() {
        return mpaRatingService.getAll();
    }

    @GetMapping("/{id}")
    public MpaRating getMpaRating(@PathVariable int id) {
        return mpaRatingService.getRatingById(id);
    }
}
