CREATE TABLE match_summaries (
    id VARCHAR(64) NOT NULL,
    match_id VARCHAR(64) NOT NULL,

    winner_team_id VARCHAR(64),
    loser_team_id VARCHAR(64),

    result_text VARCHAR(255),

    team_a_runs INT,
    team_a_overs INT,
    team_a_wickets INT,

    team_b_runs INT,
    team_b_overs INT,
    team_b_wickets INT,

    man_of_the_match_id VARCHAR(64),

    PRIMARY KEY (id),

    FOREIGN KEY (match_id) REFERENCES matches(id),
    FOREIGN KEY (winner_team_id) REFERENCES teams(id),
    FOREIGN KEY (loser_team_id) REFERENCES teams(id),
    FOREIGN KEY (man_of_the_match_id) REFERENCES players(id)
);
