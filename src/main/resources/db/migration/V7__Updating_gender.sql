ALTER TABLE cat
    ADD CONSTRAINT chk_cat_gender CHECK (cat_gender IN ('MALE', 'FEMALE'));
