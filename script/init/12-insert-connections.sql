CREATE OR REPLACE FUNCTION insert_user_connection(
    user_id_1 INT,
    user_id_2 INT
)
RETURNS INT AS
$$
DECLARE
inserted_id INT;
BEGIN
    -- Вставка данных в таблицу "user_connections"
INSERT INTO user_connections (user_id_1, user_id_2)
VALUES (user_id_1, user_id_2)
    RETURNING id INTO inserted_id;

-- Возвращаем ID вставленной строки
RETURN inserted_id;
END;
$$
LANGUAGE plpgsql;

