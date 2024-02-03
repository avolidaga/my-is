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

CREATE TYPE request_status_enum  AS ENUM (
 'DECLINED',
 'READY',
 'IN_PROGRESS'
);

CREATE TABLE IF NOT EXISTS fabric_texture (
                                              fabric_texture_id serial PRIMARY KEY,
                                              fabric_texture_name VARCHAR(100) NOT NULL,
                                              CONSTRAINT unique_fabric_texture_name UNIQUE (fabric_texture_name)
);
CREATE TABLE IF NOT EXISTS user_spacesuit_data (
   user_spacesuit_data_id serial PRIMARY KEY,
   head INT NOT NULL CHECK (head > 0),
   status request_status_enum DEFAULT  'IN_PROGRESS',
   chest INT NOT NULL CHECK (chest > 0),
   waist INT NOT NULL CHECK (waist > 0),
   hips INT NOT NULL CHECK (hips > 0),
   foot_size INT NOT NULL CHECK (foot_size > 0),
   height INT NOT NULL CHECK (height > 0 AND height <= 300),
   fabric_texture_id INT NOT NULL,
   CONSTRAINT fk_user_spacesuit_data_fabric_texture FOREIGN KEY (fabric_texture_id) REFERENCES fabric_texture(fabric_texture_id) ON DELETE CASCADE
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
CREATE TABLE IF NOT EXISTS roles (
    role_id SERIAL PRIMARY KEY,
    role_name text
);

CREATE TABLE users (
   id SERIAL PRIMARY KEY,
   username VARCHAR(255) NOT NULL,
   password VARCHAR(255) NOT NULL,
   user_spacesuit_data_id INT,
    name VARCHAR(100) ,
    CONSTRAINT fk_user_user_spacesuit_data FOREIGN KEY (user_spacesuit_data_id) REFERENCES user_spacesuit_data(user_spacesuit_data_id) ON DELETE CASCADE,
   user_data_id INT,
   CONSTRAINT fk_user_user_data FOREIGN KEY (user_data_id) REFERENCES user_data(user_data_id) ON DELETE CASCADE,
    role_id INT,
   CONSTRAINT fk_role_id FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE

);






CREATE TABLE IF NOT EXISTS user_request (
    user_request_id serial PRIMARY KEY,
    user_spacesuit_data_id INT NOT NULL,
    status request_status_enum DEFAULT  'IN_PROGRESS',
    CONSTRAINT fk_user_request_user_spacesuit_data FOREIGN KEY (user_spacesuit_data_id) REFERENCES user_spacesuit_data(user_spacesuit_data_id) ON DELETE CASCADE
);