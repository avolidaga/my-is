CREATE FUNCTION GetUserDataOnMars()
    RETURNS TABLE (
        birthdate DATE,
        sex VARCHAR(10),
        weight INT,
        height INT,
        hairColor VARCHAR(50),
        location location_enum
        ) AS
$$
BEGIN
RETURN QUERY
SELECT user_data.birth_date, user_data.sex, user_data.weight, user_data.height, user_data.hair_color, user_data.location
FROM user_data
WHERE user_data.location = 'MARS';
END
$$
LANGUAGE plpgsql;
