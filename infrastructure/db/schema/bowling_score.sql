CREATE TABLE bowling_scores (
    id VARCHAR(64) NOT NULL,
    match_id VARCHAR(64) NOT NULL,
    innings_id VARCHAR(64) NOT NULL,

    innings_number INT NOT NULL,
    bowler_id VARCHAR(64) NOT NULL,

    balls_bowled INT DEFAULT 0,
    overs INT DEFAULT 0,
    maidens INT DEFAULT 0,

    runs_conceded INT DEFAULT 0,
    wickets INT DEFAULT 0,

    economy DOUBLE DEFAULT 0,

    PRIMARY KEY (id),

    FOREIGN KEY (match_id) REFERENCES matches(id),
    FOREIGN KEY (innings_id) REFERENCES innings(id),
    FOREIGN KEY (bowler_id) REFERENCES players(id)
);
