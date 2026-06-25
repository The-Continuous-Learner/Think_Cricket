ALTER TABLE innings DROP COLUMN target;

ALTER TABLE matches DROP COLUMN follow_on_enforced;

ALTER TABLE matches
  ADD COLUMN parent_match_id VARCHAR(255),
  ADD CONSTRAINT fk_match_parent FOREIGN KEY (parent_match_id) REFERENCES matches (id);
