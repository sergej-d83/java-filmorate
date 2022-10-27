package ru.yandex.practicum.filmorate.model.film;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class Genre {
    @NonNull
    private Integer id;
    @NonNull
    private String name;
}
