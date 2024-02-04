CREATE FUNCTION GetAllConnectionsForUser(user_id integer)
    RETURNS TABLE (
        user_id2 integer
                  ) AS
$$
BEGIN
    RETURN QUERY
        SELECT user_id_2
        FROM user_connections
        WHERE user_id_1 = user_id
          AND user_id_2 = ANY (SELECT user_id_1 FROM user_connections WHERE user_id_2 = user_id)
          AND user_id_2 <> user_id
        UNION
        SELECT user_id_1
        FROM user_connections
        WHERE user_id_2 = user_id
          AND user_id_1 = ANY (SELECT user_id_2 FROM user_connections WHERE user_id_1 = user_id)
          AND user_id_1 <> user_id;
END
$$
    LANGUAGE plpgsql;




SELECT * FROM getallconnectionsforuser(1);

