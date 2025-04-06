ALTER TABLE app_user
    ADD IF NOT EXISTS user_full_name VARCHAR(255);

ALTER TABLE app_user DROP CONSTRAINT IF EXISTS uc_app_user_useremail;
ALTER TABLE app_user
    ADD CONSTRAINT uc_app_user_useremail UNIQUE (user_email);

ALTER TABLE app_user DROP CONSTRAINT IF EXISTS uc_app_user_username;
ALTER TABLE app_user
    ADD CONSTRAINT uc_app_user_username UNIQUE (user_name);

ALTER TABLE cat
    ALTER COLUMN cat_name DROP NOT NULL;

ALTER TABLE cat
    ALTER COLUMN cat_owner_user_id DROP NOT NULL;

ALTER TABLE cat_photo
    ALTER COLUMN cat_photo_created_at DROP NOT NULL;

ALTER TABLE cat_photo
    ALTER COLUMN cat_photo_url DROP NOT NULL;

ALTER TABLE event
    ALTER COLUMN event_description DROP NOT NULL;

ALTER TABLE event
    ALTER COLUMN event_location DROP NOT NULL;

ALTER TABLE event
    ALTER COLUMN event_name DROP NOT NULL;

ALTER TABLE post
    ALTER COLUMN post_created_at DROP NOT NULL;

ALTER TABLE post
    ALTER COLUMN post_text DROP NOT NULL;

ALTER TABLE recommendation
    ALTER COLUMN recommendation_category DROP NOT NULL;

ALTER TABLE recommendation
    ALTER COLUMN recommendation_product_description DROP NOT NULL;

ALTER TABLE recommendation
    ALTER COLUMN recommendation_product_link DROP NOT NULL;

ALTER TABLE recommendation
    ALTER COLUMN recommendation_product_name DROP NOT NULL;

ALTER TABLE reminder
    ALTER COLUMN reminder_info DROP NOT NULL;

ALTER TABLE reminder
    ALTER COLUMN reminder_type DROP NOT NULL;

ALTER TABLE app_user
    ALTER COLUMN user_auth_provider DROP NOT NULL;

ALTER TABLE app_user
    ALTER COLUMN user_email DROP NOT NULL;

ALTER TABLE app_user
    ALTER COLUMN user_location DROP NOT NULL;

ALTER TABLE app_user
    ALTER COLUMN user_name DROP NOT NULL;
