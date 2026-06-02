CREATE TABLE overs (
    id VARCHAR(64) NOT NULL,
    match_id VARCHAR(64) NOT NULL,
    innings_id VARCHAR(64) NOT NULL,

    over_number INT NOT NULL,
    bowler_id VARCHAR(64) NOT NULL,

    PRIMARY KEY (id),

    FOREIGN KEY (match_id) REFERENCES matches(id),
    FOREIGN KEY (innings_id) REFERENCES innings(id),
    FOREIGN KEY (bowler_id) REFERENCES players(id)
);
