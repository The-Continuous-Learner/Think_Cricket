SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE `match_squad` (
  `match_id` varchar(255) NOT NULL,
  `team_id` varchar(255) NOT NULL,
  `player_id` varchar(255) NOT NULL,
  `role` varchar(50) NOT NULL,
  PRIMARY KEY (`match_id`, `team_id`, `player_id`),
  CONSTRAINT `fk_squad_match` FOREIGN KEY (`match_id`) REFERENCES `match` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_squad_team` FOREIGN KEY (`team_id`) REFERENCES `team` (`id`),
  CONSTRAINT `fk_squad_player` FOREIGN KEY (`player_id`) REFERENCES `player` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `match_substitution` (
  `id` varchar(255) NOT NULL,
  `match_id` varchar(255) NOT NULL,
  `team_id` varchar(255) NOT NULL,
  `player_out_id` varchar(255) NOT NULL,
  `player_in_id` varchar(255) NOT NULL,
  `innings_number` int NOT NULL,
  `over_number` int NOT NULL,
  `substitution_type` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_sub_match` FOREIGN KEY (`match_id`) REFERENCES `match` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SET FOREIGN_KEY_CHECKS=1;
