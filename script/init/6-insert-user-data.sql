CREATE OR REPLACE FUNCTION insert_user_data(
    birth_date DATE,
    sex sex_enum,
    weight INT,
    height INT,
    hair_color VARCHAR,
    location location_enum
)
RETURNS INT AS
$$
DECLARE
inserted_id INT;
BEGIN
    -- Вставка данных в таблицу "user_data"
INSERT INTO user_data (birth_date, sex, weight, height, hair_color, location)
VALUES (birth_date, sex::sex_enum, weight, height, hair_color, location::location_enum)
    RETURNING user_data_id INTO inserted_id;

-- Возвращаем ID вставленной строки
RETURN inserted_id;
END;
$$
LANGUAGE plpgsql;
