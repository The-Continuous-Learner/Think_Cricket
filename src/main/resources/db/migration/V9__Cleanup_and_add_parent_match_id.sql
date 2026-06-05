-- Remove target from innings (no longer stored, calculated from innings runs)
ALTER TABLE `innings` DROP COLUMN `target`;

-- Remove follow_on_enforced from matches (follow-on handled by user providing teams explicitly)
ALTER TABLE `matches` DROP COLUMN `follow_on_enforced`;

-- Add parent_match_id to matches (used for super over linking)
ALTER TABLE `matches`
  ADD COLUMN `parent_match_id` VARCHAR(255),
  ADD CONSTRAINT `fk_match_parent` FOREIGN KEY (`parent_match_id`) REFERENCES `matches` (`id`);
