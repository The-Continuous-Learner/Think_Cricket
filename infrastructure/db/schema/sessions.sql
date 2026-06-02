CREATE TABLE `Session` (
  `token` varchar(255) NOT NULL,
  `user_id` varchar(255),
  `created_at` BIGINT NOT NULL DEFAULT (UNIX_TIMESTAMP(NOW(3)) * 1000),
  `expires_at` BIGINT NOT NULL,
  PRIMARY KEY (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;