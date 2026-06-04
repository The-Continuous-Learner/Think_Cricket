-- Rename capitalized/mismatched tables to consistent lowercase names
-- Required for Linux-based MySQL (RDS) where table names are case-sensitive

RENAME TABLE `User` TO `users`;
RENAME TABLE `Session` TO `sessions`;
RENAME TABLE `match` TO `matches`;

-- Fix column names in matches (typos in V1: teama_id/teamb_id)
ALTER TABLE `matches`
  RENAME COLUMN `teama_id` TO `team_a_id`,
  RENAME COLUMN `teamb_id` TO `team_b_id`;

-- Change status from tinyint to varchar for Hibernate EnumType.STRING
ALTER TABLE `matches` MODIFY COLUMN `status` VARCHAR(50);

-- Add missing columns to matches (entity has evolved beyond V1)
ALTER TABLE `matches`
  ADD COLUMN `format` VARCHAR(20),
  ADD COLUMN `planned_start_time` BIGINT,
  ADD COLUMN `actual_start_time` BIGINT,
  ADD COLUMN `actual_end_time` BIGINT,
  ADD COLUMN `hosted_by_user_id` VARCHAR(255),
  ADD COLUMN `started_by_user_id` VARCHAR(255),
  ADD COLUMN `ended_by_user_id` VARCHAR(255);

-- Drop stale columns from matches not present in entity
ALTER TABLE `matches`
  DROP COLUMN `start_time`,
  DROP COLUMN `end_time`,
  DROP COLUMN `first_innings_id`,
  DROP COLUMN `second_innings_id`;

-- Add runtime score columns missing from innings
ALTER TABLE `innings`
  ADD COLUMN `total_runs` INT NOT NULL DEFAULT 0,
  ADD COLUMN `wickets` INT NOT NULL DEFAULT 0,
  ADD COLUMN `extras` INT NOT NULL DEFAULT 0,
  ADD COLUMN `target` INT;

-- Add wicket FK column to ball (Ball.wicketInfo @OneToOne)
ALTER TABLE `ball`
  ADD COLUMN `wicket_id` VARCHAR(255);

-- Add balls_bowled to bowling_score (entity tracks raw balls separately from derived overs)
ALTER TABLE `bowling_score`
  ADD COLUMN `balls_bowled` INT NOT NULL DEFAULT 0;

-- Add extended detail columns missing from fall_of_wickets
ALTER TABLE `fall_of_wickets`
  ADD COLUMN `ball_number` INT,
  ADD COLUMN `bowler_id` VARCHAR(255),
  ADD COLUMN `fielder_id` VARCHAR(255),
  ADD COLUMN `ball_id` VARCHAR(255);

-- Add extended result columns missing from match_summary
ALTER TABLE `match_summary`
  ADD COLUMN `loser_team_id` VARCHAR(255),
  ADD COLUMN `result_text` VARCHAR(500),
  ADD COLUMN `team_a_overs` INT NOT NULL DEFAULT 0,
  ADD COLUMN `team_a_wickets` INT NOT NULL DEFAULT 0,
  ADD COLUMN `team_b_overs` INT NOT NULL DEFAULT 0,
  ADD COLUMN `team_b_wickets` INT NOT NULL DEFAULT 0;
