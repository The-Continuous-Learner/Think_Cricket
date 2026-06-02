ALTER TABLE ball
ADD CONSTRAINT fk_ball_match
FOREIGN KEY (match_id) REFERENCES matches(id);

ALTER TABLE ball
ADD CONSTRAINT fk_ball_innings
FOREIGN KEY (innings_id) REFERENCES innings(id);

ALTER TABLE ball
ADD CONSTRAINT fk_ball_over
FOREIGN KEY (over_id) REFERENCES overs(id);

ALTER TABLE ball
ADD CONSTRAINT fk_ball_batsman
FOREIGN KEY (batsman_id) REFERENCES player(id);

ALTER TABLE ball
ADD CONSTRAINT fk_ball_non_striker
FOREIGN KEY (non_striker_id) REFERENCES player(id);

ALTER TABLE ball
ADD CONSTRAINT fk_ball_bowler
FOREIGN KEY (bowler_id) REFERENCES player(id);

ALTER TABLE ball
ADD CONSTRAINT fk_ball_fielder
FOREIGN KEY (fielder_id) REFERENCES player(id);
