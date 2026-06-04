
-- Drop the check constraint left over from V1 when matches.status was tinyint
-- V2 changed the column to VARCHAR(50) for Hibernate EnumType.STRING but did not drop this constraint
ALTER TABLE `matches` DROP CHECK `matches_chk_1`;
