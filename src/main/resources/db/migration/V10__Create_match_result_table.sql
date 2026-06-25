CREATE TABLE match_result (
  id VARCHAR(255) NOT NULL,
  match_id VARCHAR(255) NOT NULL,
  winner_team_id VARCHAR(255),
  loser_team_id VARCHAR(255),
  is_draw boolean NOT NULL DEFAULT false,
  result_text VARCHAR(500),
  decided_by_super_over boolean NOT NULL DEFAULT false,
  PRIMARY KEY (id),
  CONSTRAINT uq_match_result_match UNIQUE (match_id),
  CONSTRAINT fk_match_result_match FOREIGN KEY (match_id) REFERENCES matches (id) ON DELETE CASCADE
);
