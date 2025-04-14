ALTER TABLE recommendation
    ADD recommendation_user_id BIGINT;

ALTER TABLE recommendation
    ALTER COLUMN recommendation_user_id SET NOT NULL;

ALTER TABLE reminder
    ADD reminder_user_id BIGINT;

ALTER TABLE reminder
    ALTER COLUMN reminder_user_id SET NOT NULL;

ALTER TABLE recommendation
    ADD CONSTRAINT FK_RECOMMENDATION_ON_RECOMMENDATION_USER FOREIGN KEY (recommendation_user_id) REFERENCES app_user (user_id);

ALTER TABLE reminder
    ADD CONSTRAINT FK_REMINDER_ON_REMINDER_USER FOREIGN KEY (reminder_user_id) REFERENCES app_user (user_id);

ALTER TABLE cat_photo
    ALTER COLUMN cat_photo_caption TYPE VARCHAR(1000) USING (cat_photo_caption::VARCHAR(1000));

ALTER TABLE cat_photo
    ALTER COLUMN cat_photo_url TYPE VARCHAR(500) USING (cat_photo_url::VARCHAR(500));
