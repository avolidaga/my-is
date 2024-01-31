CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL
);

CREATE TABLE shooting (
                          id SERIAL PRIMARY KEY,
                          coach VARCHAR,
                          isKronbars BOOLEAN
);