CREATE TABLE matches (
    id VARCHAR(64) NOT NULL,
    team_a_id VARCHAR(64) NOT NULL,
    team_b_id VARCHAR(64) NOT NULL,

    overs INT NOT NULL,
    status VARCHAR(50) NOT NULL,

    toss_winner_team_id VARCHAR(64),
    toss_decision VARCHAR(10),

    hosted_by_user_id VARCHAR(64) NOT NULL,

    PRIMARY KEY (id),

    FOREIGN KEY (team_a_id) REFERENCES teams(id),
    FOREIGN KEY (team_b_id) REFERENCES teams(id),
    FOREIGN KEY (toss_winner_team_id) REFERENCES teams(id),
    FOREIGN KEY (hosted_by_user_id) REFERENCES users(id)
);
