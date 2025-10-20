
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(255) NOT NULL,
    login VARCHAR(255) NOT NULL UNIQUE,
    birthday DATE
);

CREATE TABLE IF NOT EXISTS friendships (
    requester_id INTEGER NOT NULL,
    addressee_id INTEGER NOT NULL,
    PRIMARY KEY (requester_id, addressee_id),
    CONSTRAINT fk_friendships_requester FOREIGN KEY (requester_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_friendships_addressee FOREIGN KEY (addressee_id) REFERENCES users(id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS ratings (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);


CREATE TABLE IF NOT EXISTS films (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    release_date DATE,
    duration INTEGER CHECK (duration > 0),
    rating_id INTEGER,
    CONSTRAINT fk_films_rating FOREIGN KEY (rating_id) REFERENCES ratings (id) ON DELETE SET NULL
);


CREATE TABLE IF NOT EXISTS genres (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);


CREATE TABLE IF NOT EXISTS film_genres (
    id SERIAL PRIMARY KEY,
    film_id INTEGER NOT NULL,
    genre_id INTEGER NOT NULL,
    CONSTRAINT fk_film_genres_film FOREIGN KEY (film_id) REFERENCES films (id) ON DELETE CASCADE,
    CONSTRAINT fk_film_genres_genre FOREIGN KEY (genre_id) REFERENCES genres (id) ON DELETE CASCADE,
    CONSTRAINT uq_film_genres UNIQUE (film_id, genre_id)
);


CREATE TABLE IF NOT EXISTS likes_films (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    film_id INTEGER NOT NULL,
    CONSTRAINT fk_likes_films_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_likes_films_film FOREIGN KEY (film_id) REFERENCES films (id) ON DELETE CASCADE,
    CONSTRAINT uq_likes_films UNIQUE (user_id, film_id)
);


