CREATE OR REPLACE FUNCTION update_user_spacesuit_data_status_declined()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.status = 'DECLINED' THEN
UPDATE user_spacesuit_data
SET status = 'DECLINED'
WHERE user_spacesuit_data_id = NEW.user_spacesuit_data_id;
END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_user_spacesuit_data_status_declined_trigger
    AFTER UPDATE ON user_request
    FOR EACH ROW
    EXECUTE FUNCTION update_user_spacesuit_data_status_declined();


UPDATE user_request
SET status = 'READY' WHERE user_spacesuit_data_id = 1;