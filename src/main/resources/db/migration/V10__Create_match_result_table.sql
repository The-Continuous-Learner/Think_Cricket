CREATE TABLE `match_result` (
  `id` VARCHAR(255) NOT NULL,
  `match_id` VARCHAR(255) NOT NULL,
  `winner_team_id` VARCHAR(255),
  `loser_team_id` VARCHAR(255),
  `is_draw` TINYINT(1) NOT NULL DEFAULT 0,
  `result_text` VARCHAR(500),
  `decided_by_super_over` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_match_result_match` (`match_id`),
  CONSTRAINT `fk_match_result_match` FOREIGN KEY (`match_id`) REFERENCES `matches` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
