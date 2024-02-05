CREATE FUNCTION GetUserDataOnMars()
    RETURNS TABLE (
        user_data_id integer
        ) AS
$$
BEGIN
RETURN QUERY
SELECT user_data.user_data_id
FROM user_data
WHERE user_data.location = 'MARS';
END
$$
LANGUAGE plpgsql;


