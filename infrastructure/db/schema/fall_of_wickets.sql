CREATE TABLE fall_of_wickets (
    id VARCHAR(64) NOT NULL,
    match_id VARCHAR(64) NOT NULL,
    innings_id VARCHAR(64) NOT NULL,

    innings_number INT NOT NULL,
    wicket_number INT NOT NULL,

    team_score_at_fall INT NOT NULL,
    over_number INT NOT NULL,
    ball_number INT NOT NULL,

    player_out_id VARCHAR(64) NOT NULL,
    bowler_id VARCHAR(64) NOT NULL,
    fielder_id VARCHAR(64),

    ball_id VARCHAR(64) NOT NULL,

    PRIMARY KEY (id),

    FOREIGN KEY (match_id) REFERENCES matches(id),
    FOREIGN KEY (innings_id) REFERENCES innings(id),
    FOREIGN KEY (player_out_id) REFERENCES players(id),
    FOREIGN KEY (bowler_id) REFERENCES players(id),
    FOREIGN KEY (fielder_id) REFERENCES players(id),
    FOREIGN KEY (ball_id) REFERENCES balls(id)
);
