CREATE TABLE batting_scores (
    id VARCHAR(64) NOT NULL,
    match_id VARCHAR(64) NOT NULL,
    innings_id VARCHAR(64) NOT NULL,

    innings_number INT NOT NULL,
    player_id VARCHAR(64) NOT NULL,
    batting_position INT NOT NULL,

    runs INT DEFAULT 0,
    balls INT DEFAULT 0,
    fours INT DEFAULT 0,
    sixes INT DEFAULT 0,

    out BOOLEAN DEFAULT FALSE,
    dismissal_type VARCHAR(50),
    dismissal_ball_id VARCHAR(64),

    strike_rate DOUBLE DEFAULT 0,

    PRIMARY KEY (id),

    FOREIGN KEY (match_id) REFERENCES matches(id),
    FOREIGN KEY (innings_id) REFERENCES innings(id),
    FOREIGN KEY (player_id) REFERENCES players(id),
    FOREIGN KEY (dismissal_ball_id) REFERENCES balls(id)
);
