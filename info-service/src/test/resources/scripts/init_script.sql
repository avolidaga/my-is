CREATE TABLE info (
  location_id SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  temperature DOUBLE PRECISION
);

INSERT INTO info (name, description, temperature)
            SELECT 'Earth', 'some place 1', 15.0
            WHERE NOT EXISTS (
            SELECT 1 FROM info WHERE name = 'Earth'
            );

INSERT INTO info (name, description, temperature)
            SELECT 'Mars', 'some place 2', 10.0
            WHERE NOT EXISTS (
            SELECT 1 FROM info WHERE name = 'Mars'
            );