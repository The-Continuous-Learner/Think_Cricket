CREATE TABLE current_batting_state (
    innings_id   VARCHAR(255) PRIMARY KEY,
    striker_id   VARCHAR(255) NOT NULL,
    non_striker_id VARCHAR(255) NOT NULL
);
