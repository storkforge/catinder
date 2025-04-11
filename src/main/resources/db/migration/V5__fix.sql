ALTER TABLE app_user
    ADD IF NOT EXISTS user_password VARCHAR(255);

ALTER TABLE app_user
    DROP COLUMN IF EXISTS password;

ALTER TABLE cat
    ALTER COLUMN cat_owner_user_id SET NOT NULL;
