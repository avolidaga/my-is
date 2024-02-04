CREATE FUNCTION GetAllUserData()
    RETURNS TABLE (
        user_data_id integer
        ) AS
$$
BEGIN
RETURN QUERY
SELECT user_data.user_data_id
FROM user_data;
END
$$
LANGUAGE plpgsql;