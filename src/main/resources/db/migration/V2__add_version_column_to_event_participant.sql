ALTER TABLE event_participant
    ADD COLUMN IF NOT EXISTS version INT DEFAULT 0;
