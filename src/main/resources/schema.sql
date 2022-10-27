DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS friendships CASCADE;
DROP TABLE IF EXISTS mpa_ratings CASCADE;
DROP TABLE IF EXISTS films CASCADE;
DROP TABLE IF EXISTS genres CASCADE;
DROP TABLE IF EXISTS film_genres CASCADE;
DROP TABLE IF EXISTS film_likes CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    user_id   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email     VARCHAR NOT NULL UNIQUE,
    login     VARCHAR NOT NULL UNIQUE,
    user_name VARCHAR,
    birthday DATE
);

CREATE TABLE IF NOT EXISTS friendships
(
    user_id      BIGINT  NOT NULL REFERENCES users (user_id),
    friend_id    BIGINT  NOT NULL REFERENCES users (user_id),
    is_confirmed BOOLEAN NOT NULL,
    PRIMARY KEY (user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS mpa_ratings
(
    mpa_rating_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    rating_name   VARCHAR NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS films
(
    film_id      BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_name    VARCHAR NOT NULL,
    description  VARCHAR(200),
    release_date DATE,
    duration     INTEGER CHECK (duration > 0),
    rating_id    INTEGER REFERENCES mpa_ratings (mpa_rating_id)
);

CREATE TABLE IF NOT EXISTS genres
(
    genre_id   INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    genre_name VARCHAR NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS film_genres
(
    film_id  BIGINT  NOT NULL REFERENCES films (film_id),
    genre_id INTEGER NOT NULL REFERENCES genres (genre_id)
);

CREATE TABLE IF NOT EXISTS film_likes
(
    film_id BIGINT NOT NULL REFERENCES films (film_id),
    user_id BIGINT NOT NULL REFERENCES users (user_id)
);