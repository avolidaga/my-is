CREATE FUNCTION getReadyUserRequest()
RETURNS TABLE (
    user_request_id integer
    ) AS
$$
BEGIN
RETURN QUERY
SELECT user_request.user_request_id
FROM user_request
WHERE user_request.status = 'READY';
END
$$
LANGUAGE plpgsql;
