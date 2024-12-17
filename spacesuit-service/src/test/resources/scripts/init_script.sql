CREATE TABLE IF NOT EXISTS roles (
    role_id SERIAL PRIMARY KEY,
    role_name text
);

CREATE TABLE IF NOT EXISTS users (
   id SERIAL PRIMARY KEY,
   username VARCHAR(255) NOT NULL,
   password VARCHAR(255) NOT NULL,
   role_id INT default 1,
   CONSTRAINT fk_role_id FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_data (
   user_data_id serial PRIMARY KEY,
   user_id int NOT NULL,
    birth_date DATE NOT NULL,
    sex VARCHAR(20) NOT NULL,
    weight INT NOT NULL CHECK (weight > 0),
    height INT NOT NULL CHECK (height > 0 AND height <= 300),
    hair_color VARCHAR(255) NOT NULL,
    firstname VARCHAR(255),
    lastname VARCHAR(255),
    created_at DATE NOT NULL,
    updated_at DATE,
    CONSTRAINT fk_user_data FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );

 CREATE TABLE user_connections (
       id SERIAL PRIMARY KEY,
       user_id_1 INTEGER REFERENCES users(id),
       user_id_2 INTEGER REFERENCES users(id),
       match_date DATE NOT NULL
 );

 INSERT INTO roles (role_name)
            SELECT 'ADMIN'
            WHERE NOT EXISTS (
            SELECT 1 FROM roles WHERE role_name = 'ADMIN'
            );

 INSERT INTO roles (role_name)
             SELECT 'USER'
             WHERE NOT EXISTS (
             SELECT 1 FROM roles WHERE role_name = 'USER'
             );

 INSERT INTO users (username, password, role_id)
            SELECT 'user', '$2a$10$ndxcI.OCleE64FYaOA0HneYDKiWpuJ6VxdRH9SlkIovV0/uSR84fq', 2
            WHERE NOT EXISTS (
            SELECT 1 FROM users WHERE username = 'user'
            );

 CREATE TABLE location (
   location_id SERIAL PRIMARY KEY,
   name VARCHAR(255) NOT NULL,
   description TEXT,
   temperature DOUBLE PRECISION
 );

 INSERT INTO location (name, description, temperature)
             SELECT 'Earth', 'some place 1', 15.0
             WHERE NOT EXISTS (
             SELECT 1 FROM location WHERE name = 'Earth'
             );

 INSERT INTO location (name, description, temperature)
             SELECT 'Mars', 'some place 2', 10.0
             WHERE NOT EXISTS (
             SELECT 1 FROM location WHERE name = 'Mars'
             );

 CREATE TABLE user_data_location (
       location_id INTEGER REFERENCES location(location_id),
       user_data_id INTEGER REFERENCES user_data(user_data_id),
       CONSTRAINT pk_user_data_location PRIMARY KEY(location_id, user_data_id)
 );


CREATE TABLE IF NOT EXISTS fabric_texture (
      fabric_texture_id serial PRIMARY KEY,
      fabric_texture_name VARCHAR(100) NOT NULL,
      fabric_texture_description VARCHAR(255),
      CONSTRAINT unique_fabric_texture_name UNIQUE (fabric_texture_name)
);

 INSERT INTO fabric_texture (fabric_texture_name, fabric_texture_description)
            SELECT 'silk', 'some description for silk'
                WHERE NOT EXISTS (
                SELECT 1 FROM fabric_texture WHERE fabric_texture_name = 'silk'
            );

 INSERT INTO fabric_texture (fabric_texture_name, fabric_texture_description)
             SELECT 'cotton', 'some description for cotton'
                 WHERE NOT EXISTS (
                 SELECT 1 FROM fabric_texture WHERE fabric_texture_name = 'cotton'
             );

CREATE TABLE IF NOT EXISTS user_spacesuit_data (
   user_spacesuit_data_id serial PRIMARY KEY,
   user_id INTEGER REFERENCES users(id),
   head INT NOT NULL CHECK (head > 0),
   status VARCHAR(50) DEFAULT 'NEW',
   chest INT NOT NULL CHECK (chest > 0),
   waist INT NOT NULL CHECK (waist > 0),
   hips INT NOT NULL CHECK (hips > 0),
   foot_size INT NOT NULL CHECK (foot_size > 0),
   height INT NOT NULL CHECK (height > 0 AND height <= 300),
   fabric_texture_id INT NOT NULL,
   created_at DATE NOT NULL,
   updated_at DATE,
   CONSTRAINT fk_user_spacesuit_data_fabric_texture FOREIGN KEY (fabric_texture_id) REFERENCES fabric_texture(fabric_texture_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_request (
    user_request_id serial PRIMARY KEY,
    user_spacesuit_data_id INT NOT NULL,
    status VARCHAR(50) DEFAULT  'NEW',
    created_at DATE NOT NULL,
    updated_at DATE,
    CONSTRAINT fk_user_request_user_spacesuit_data FOREIGN KEY (user_spacesuit_data_id) REFERENCES user_spacesuit_data(user_spacesuit_data_id) ON DELETE CASCADE
);