CREATE TABLE `toss` (
  `id` VARCHAR(255) NOT NULL,
  `match_id` VARCHAR(255) NOT NULL,
  `toss_result` VARCHAR(10) NOT NULL,
  `winner_team_id` VARCHAR(255) NOT NULL,
  `decision` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_toss_match` (`match_id`),
  CONSTRAINT `fk_toss_match` FOREIGN KEY (`match_id`) REFERENCES `matches` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_toss_winner` FOREIGN KEY (`winner_team_id`) REFERENCES `team` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
