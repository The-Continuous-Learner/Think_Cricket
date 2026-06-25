CREATE TABLE toss (
  id VARCHAR(255) NOT NULL,
  match_id VARCHAR(255) NOT NULL,
  toss_result VARCHAR(10) NOT NULL,
  winner_team_id VARCHAR(255) NOT NULL,
  decision VARCHAR(20) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT uq_toss_match UNIQUE (match_id),
  CONSTRAINT fk_toss_match FOREIGN KEY (match_id) REFERENCES matches (id) ON DELETE CASCADE,
  CONSTRAINT fk_toss_winner FOREIGN KEY (winner_team_id) REFERENCES team (id)
);
