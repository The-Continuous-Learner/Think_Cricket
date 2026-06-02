CREATE TABLE balls (
    id VARCHAR(64) NOT NULL,
    match_id VARCHAR(64) NOT NULL,
    innings_id VARCHAR(64) NOT NULL,
    over_id VARCHAR(64) NOT NULL,

    over_number INT NOT NULL,
    ball_number INT NOT NULL,

    batsman_id VARCHAR(64) NOT NULL,
    bowler_id VARCHAR(64) NOT NULL,

    runs INT DEFAULT 0,
    extra_type VARCHAR(50),
    extra_runs INT DEFAULT 0,

    is_wicket BOOLEAN DEFAULT FALSE,
    wicket_type VARCHAR(50),
    fielder_id VARCHAR(64),

    PRIMARY KEY (id),

    FOREIGN KEY (match_id) REFERENCES matches(id),
    FOREIGN KEY (innings_id) REFERENCES innings(id),
    FOREIGN KEY (over_id) REFERENCES overs(id),
    FOREIGN KEY (batsman_id) REFERENCES players(id),
    FOREIGN KEY (bowler_id) REFERENCES players(id),
    FOREIGN KEY (fielder_id) REFERENCES players(id)
);
