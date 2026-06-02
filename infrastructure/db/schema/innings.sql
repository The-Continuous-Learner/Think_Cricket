CREATE TABLE innings (
    id VARCHAR(64) NOT NULL,
    match_id VARCHAR(64) NOT NULL,

    innings_number INT NOT NULL,

    batting_team_id VARCHAR(64) NOT NULL,
    bowling_team_id VARCHAR(64) NOT NULL,

    total_runs INT DEFAULT 0,
    total_wickets INT DEFAULT 0,
    total_overs INT DEFAULT 0,

    PRIMARY KEY (id),
    FOREIGN KEY (match_id) REFERENCES matches(id),
    FOREIGN KEY (batting_team_id) REFERENCES teams(id),
    FOREIGN KEY (bowling_team_id) REFERENCES teams(id)
);
