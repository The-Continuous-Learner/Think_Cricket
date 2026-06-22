ALTER TABLE `match_squad` DROP FOREIGN KEY `fk_squad_match`;
ALTER TABLE `match_squad` ADD CONSTRAINT `fk_squad_match` FOREIGN KEY (`match_id`) REFERENCES `matches` (`id`) ON DELETE CASCADE;

ALTER TABLE `match_substitution` DROP FOREIGN KEY `fk_sub_match`;
ALTER TABLE `match_substitution` ADD CONSTRAINT `fk_sub_match` FOREIGN KEY (`match_id`) REFERENCES `matches` (`id`) ON DELETE CASCADE;
