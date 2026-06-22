ALTER TABLE `match_squad`
    ADD COLUMN `is_captain` tinyint(1) NOT NULL DEFAULT 0,
    ADD COLUMN `is_vice_captain` tinyint(1) NOT NULL DEFAULT 0;
