ALTER TABLE event_participant
    ADD IF NOT EXISTS version INTEGER;

ALTER TABLE event_participant
    ALTER COLUMN version SET NOT NULL;
