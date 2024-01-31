CREATE OR REPLACE FUNCTION insert_shooting_data(username VARCHAR, isKronbars BOOLEAN)
RETURNS INT AS
$$
DECLARE
inserted_id INT;
BEGIN
    -- Вставка данных в таблицу "shooting"
INSERT INTO shooting (coach, isKronbars) VALUES (username, isKronbars) RETURNING id INTO inserted_id;

-- Возвращаем ID вставленной строки
RETURN inserted_id;
END;
$$
LANGUAGE plpgsql;