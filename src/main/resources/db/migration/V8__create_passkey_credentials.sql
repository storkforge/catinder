CREATE TABLE passkey_credentials(
    credential_id   VARCHAR(255) PRIMARY KEY,
    user_name       VARCHAR(255) NOT NULL,
    public_key      TEXT         NOT NULL,
    signature_count BIGINT,
    created_at      TIMESTAMP,
    updated_at      TIMESTAMP
);
