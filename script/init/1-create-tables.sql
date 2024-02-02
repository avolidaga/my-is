CREATE TABLE shooting (
                          id SERIAL PRIMARY KEY,
                          coach VARCHAR,
                          isKronbars BOOLEAN
);

CREATE TYPE sex_enum AS ENUM (
 'MEN',
 'WOMEN'
);

CREATE TYPE location_enum AS ENUM (
 'EARTH',
 'MARS'
);

CREATE TABLE IF NOT EXISTS user_data (
   user_data_id serial PRIMARY KEY,
    birth_date DATE NOT NULL CHECK (birth_date <= CURRENT_DATE - 18 * INTERVAL '1 year'),
    sex sex_enum NOT NULL,
    weight INT NOT NULL CHECK (weight > 0),
    height INT NOT NULL CHECK (height > 0 AND height <= 300),
    hair_color VARCHAR(255) NOT NULL,
    location location_enum NOT NULL CHECK (location IN ('EARTH', 'MARS'))
    );

CREATE TABLE users (
   id SERIAL PRIMARY KEY,
   username VARCHAR(255) NOT NULL,
   password VARCHAR(255) NOT NULL,
   user_data_id INT,
   CONSTRAINT fk_user_user_data FOREIGN KEY (user_data_id) REFERENCES user_data(user_data_id) ON DELETE CASCADE

);