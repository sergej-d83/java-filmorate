package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class Like {
    @NonNull
    private Integer filmId;
    @NonNull
    private Integer userId;
}
