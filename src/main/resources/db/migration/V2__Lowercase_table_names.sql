ALTER TABLE matches RENAME COLUMN teama_id TO team_a_id;
ALTER TABLE matches RENAME COLUMN teamb_id TO team_b_id;

ALTER TABLE matches ALTER COLUMN status TYPE VARCHAR(50) USING status::text;

ALTER TABLE matches
  ADD COLUMN format VARCHAR(20),
  ADD COLUMN planned_start_time BIGINT,
  ADD COLUMN actual_start_time BIGINT,
  ADD COLUMN actual_end_time BIGINT,
  ADD COLUMN hosted_by_user_id VARCHAR(255),
  ADD COLUMN started_by_user_id VARCHAR(255),
  ADD COLUMN ended_by_user_id VARCHAR(255);

ALTER TABLE matches
  DROP COLUMN start_time,
  DROP COLUMN end_time,
  DROP COLUMN first_innings_id,
  DROP COLUMN second_innings_id;

ALTER TABLE innings
  ADD COLUMN total_runs INT NOT NULL DEFAULT 0,
  ADD COLUMN wickets INT NOT NULL DEFAULT 0,
  ADD COLUMN extras INT NOT NULL DEFAULT 0,
  ADD COLUMN target INT;

ALTER TABLE ball
  ADD COLUMN wicket_id VARCHAR(255);

ALTER TABLE bowling_score
  ADD COLUMN balls_bowled INT NOT NULL DEFAULT 0;

ALTER TABLE fall_of_wickets
  ADD COLUMN ball_number INT,
  ADD COLUMN bowler_id VARCHAR(255),
  ADD COLUMN fielder_id VARCHAR(255),
  ADD COLUMN ball_id VARCHAR(255);

ALTER TABLE match_summary
  ADD COLUMN loser_team_id VARCHAR(255),
  ADD COLUMN result_text VARCHAR(500),
  ADD COLUMN team_a_overs INT NOT NULL DEFAULT 0,
  ADD COLUMN team_a_wickets INT NOT NULL DEFAULT 0,
  ADD COLUMN team_b_overs INT NOT NULL DEFAULT 0,
  ADD COLUMN team_b_wickets INT NOT NULL DEFAULT 0;
