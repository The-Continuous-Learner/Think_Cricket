ALTER TABLE match_squad
    ADD COLUMN is_captain boolean NOT NULL DEFAULT false,
    ADD COLUMN is_vice_captain boolean NOT NULL DEFAULT false;
