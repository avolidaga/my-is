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