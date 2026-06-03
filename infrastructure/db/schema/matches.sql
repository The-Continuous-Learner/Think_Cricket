CREATE TABLE matches (
    id VARCHAR(64) PRIMARY KEY,

    team_a_id VARCHAR(64) NOT NULL,
    team_b_id VARCHAR(64) NOT NULL,

    format VARCHAR(20) NOT NULL,

    status VARCHAR(20) NOT NULL,

    planned_start_time BIGINT NOT NULL,
    actual_start_time BIGINT,
    actual_end_time BIGINT,

    hosted_by_user_id VARCHAR(64) NOT NULL,
    started_by_user_id VARCHAR(64),
    ended_by_user_id VARCHAR(64)
);
