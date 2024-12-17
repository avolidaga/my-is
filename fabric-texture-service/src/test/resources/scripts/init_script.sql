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