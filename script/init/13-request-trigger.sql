CREATE OR REPLACE FUNCTION add_to_another_table()
RETURNS TRIGGER AS $$
BEGIN
INSERT INTO user_request (user_spacesuit_data_id)
VALUES (NEW.user_spacesuit_data_id);

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER insert_another_table_trigger
    AFTER INSERT ON user_spacesuit_data
    FOR EACH ROW
    EXECUTE FUNCTION add_to_another_table();
