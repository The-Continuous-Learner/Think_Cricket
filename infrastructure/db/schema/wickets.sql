CREATE TABLE wickets (
    id VARCHAR(64) NOT NULL,
    ball_id VARCHAR(64) NOT NULL,

    player_out_id VARCHAR(64) NOT NULL,
    bowler_id VARCHAR(64) NOT NULL,
    fielder_id VARCHAR(64),

    wicket_type VARCHAR(50),

    PRIMARY KEY (id),

    FOREIGN KEY (ball_id) REFERENCES balls(id),
    FOREIGN KEY (player_out_id) REFERENCES players(id),
    FOREIGN KEY (bowler_id) REFERENCES players(id),
    FOREIGN KEY (fielder_id) REFERENCES players(id)
);
