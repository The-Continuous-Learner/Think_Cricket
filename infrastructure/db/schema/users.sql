CREATE TABLE users (
    id VARCHAR(64) NOT NULL,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    verified BOOLEAN NOT NULL DEFAULT FALSE,

    PRIMARY KEY (id),
    UNIQUE KEY uq_user_email (email),
    UNIQUE KEY uq_user_username (username)
);
