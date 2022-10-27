package ru.yandex.practicum.filmorate.model.film;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private Integer id;

    @NotNull(message = "Обязательно должно быть название.")
    @NotBlank(message = "Название не может быть пустым.")
    private String name;

    @NotNull(message = "Обязательно должно быть описание.")
    @Size(max = 200, message = "Максимальная длина описания — 200 символов.")
    private String description;

    @NotNull(message = "Обязательно должна быть дата релиза.")
    private LocalDate releaseDate;

    @Min(value = 1, message = "Продолжительность фильма должна быть положительной.")
    private int duration;

    @NotNull(message = "Обязательно должен быть указан рейтинг.")
    private MpaRating mpaRating;

    private Set<Genre> genre = new HashSet<>();
    private Set<Integer> likes = new HashSet<>();

//    public Film(Integer id, String name, String description, LocalDate releaseDate, int duration, int ratingId) {
//        this.id = id;
//        this.name = name;
//        this.description = description;
//        this.releaseDate = releaseDate;
//        this.duration = duration;
//        this.ratingId = ratingId;
//    }
}
