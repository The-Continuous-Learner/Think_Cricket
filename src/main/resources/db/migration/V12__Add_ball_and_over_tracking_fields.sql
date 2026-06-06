-- Ball: extra_runs (wide/bye runs separate from bat runs), boundary_type, bowler_id per ball
ALTER TABLE `ball`
  ADD COLUMN `extra_runs` INT NOT NULL DEFAULT 0,
  ADD COLUMN `boundary_type` VARCHAR(10),
  ADD COLUMN `bowler_id` VARCHAR(255);

-- Over: track legal ball count separately for auto-completion logic
ALTER TABLE `over`
  ADD COLUMN `legal_ball_count` INT NOT NULL DEFAULT 0;
