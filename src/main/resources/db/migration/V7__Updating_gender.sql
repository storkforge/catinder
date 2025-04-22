UPDATE cat SET cat_gender = 'FEMALE' WHERE cat_gender NOT IN ('MALE', 'FEMALE') OR cat_gender IS NULL;

ALTER TABLE cat
    ADD CONSTRAINT chk_cat_gender CHECK (cat_gender IN ('MALE', 'FEMALE'));
