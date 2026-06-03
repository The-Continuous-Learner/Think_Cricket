CREATE TABLE player_team_mapper (
    player_id VARCHAR(64) NOT NULL,
    team_id VARCHAR(64) NOT NULL,

    PRIMARY KEY (team_id, player_id),

    FOREIGN KEY (player_id) REFERENCES player(id),
    FOREIGN KEY (team_id) REFERENCES team(id)
);