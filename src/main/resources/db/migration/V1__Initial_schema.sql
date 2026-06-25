CREATE TABLE users (
  id varchar(255) NOT NULL,
  email varchar(255) NOT NULL UNIQUE,
  username varchar(255) NOT NULL UNIQUE,
  password_hash varchar(255) NOT NULL,
  verified boolean NOT NULL DEFAULT false,
  PRIMARY KEY (id)
);

CREATE TABLE sessions (
  token varchar(255) NOT NULL,
  user_id varchar(255) NOT NULL,
  created_at BIGINT NOT NULL,
  expires_at BIGINT NOT NULL,
  PRIMARY KEY (token),
  CONSTRAINT fk_session_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
CREATE INDEX idx_user_id ON sessions(user_id);

CREATE TABLE team (
  id varchar(255) NOT NULL,
  name varchar(255),
  description varchar(255),
  PRIMARY KEY (id)
);

CREATE TABLE player (
  id varchar(255) NOT NULL,
  name varchar(255),
  age int,
  gender varchar(50),
  type varchar(50),
  PRIMARY KEY (id)
);

CREATE TABLE player_team_mapper (
  team_id varchar(255) NOT NULL,
  player_id varchar(255) NOT NULL,
  PRIMARY KEY (team_id, player_id),
  CONSTRAINT fk_mapper_team_id FOREIGN KEY (team_id) REFERENCES team (id) ON DELETE CASCADE,
  CONSTRAINT fk_mapper_player_id FOREIGN KEY (player_id) REFERENCES player (id) ON DELETE CASCADE
);

CREATE TABLE matches (
  id varchar(255) NOT NULL,
  start_time timestamp,
  end_time timestamp,
  status smallint,
  teama_id varchar(255),
  teamb_id varchar(255),
  first_innings_id varchar(255),
  second_innings_id varchar(255),
  PRIMARY KEY (id),
  CONSTRAINT fk_match_teama FOREIGN KEY (teama_id) REFERENCES team (id),
  CONSTRAINT fk_match_teamb FOREIGN KEY (teamb_id) REFERENCES team (id)
);

CREATE TABLE innings (
  id varchar(255) NOT NULL,
  match_id varchar(255),
  innings_number int NOT NULL,
  batting_team_id varchar(255),
  bowling_team_id varchar(255),
  PRIMARY KEY (id),
  CONSTRAINT fk_innings_match FOREIGN KEY (match_id) REFERENCES matches (id) ON DELETE CASCADE,
  CONSTRAINT fk_innings_batting_team FOREIGN KEY (batting_team_id) REFERENCES team (id),
  CONSTRAINT fk_innings_bowling_team FOREIGN KEY (bowling_team_id) REFERENCES team (id)
);

CREATE TABLE "over" (
  id varchar(255) NOT NULL,
  match_id varchar(255),
  innings_id varchar(255),
  innings_number int NOT NULL,
  over_number int NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_over_innings FOREIGN KEY (innings_id) REFERENCES innings (id) ON DELETE CASCADE
);

CREATE TABLE ball (
  id varchar(255) NOT NULL,
  match_id varchar(255),
  innings_id varchar(255),
  over_id varchar(255),
  innings_number int NOT NULL,
  over_number int NOT NULL,
  ball_number int NOT NULL,
  legal_delivery boolean NOT NULL,
  runs int NOT NULL,
  extra_type varchar(50),
  batsman_id varchar(255),
  non_striker_id varchar(255),
  wicket boolean NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_ball_over FOREIGN KEY (over_id) REFERENCES "over" (id)
);

CREATE TABLE wicket (
  id varchar(255) NOT NULL,
  match_id varchar(255),
  innings_id varchar(255),
  over_id varchar(255),
  ball_id varchar(255),
  player_out_id varchar(255),
  bowler_id varchar(255),
  fielder_id varchar(255),
  type varchar(50),
  PRIMARY KEY (id)
);

CREATE TABLE batting_score (
  id varchar(255) NOT NULL,
  match_id varchar(255),
  innings_id varchar(255),
  innings_number int NOT NULL,
  player_id varchar(255),
  batting_position int NOT NULL,
  runs int NOT NULL,
  balls int NOT NULL,
  fours int NOT NULL,
  sixes int NOT NULL,
  "out" boolean NOT NULL,
  dismissal_type varchar(255),
  dismissal_ball_id varchar(255),
  strike_rate double precision NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE bowling_score (
  id varchar(255) NOT NULL,
  match_id varchar(255),
  innings_id varchar(255),
  innings_number int NOT NULL,
  player_id varchar(255),
  overs int NOT NULL,
  maidens int NOT NULL,
  runs int NOT NULL,
  wickets int NOT NULL,
  economy double precision NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE fall_of_wickets (
  id varchar(255) NOT NULL,
  match_id varchar(255),
  innings_id varchar(255),
  innings_number int NOT NULL,
  wicket_number int NOT NULL,
  player_out_id varchar(255),
  runs_at_fall int NOT NULL,
  over_at_fall int NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE match_summary (
  id varchar(255) NOT NULL,
  match_id varchar(255),
  team_a_id varchar(255),
  team_b_id varchar(255),
  team_a_runs int NOT NULL,
  team_b_runs int NOT NULL,
  winner_id varchar(255),
  player_of_match_id varchar(255),
  match_status varchar(50),
  PRIMARY KEY (id)
);

CREATE TABLE innings_overs (
  innings_id varchar(255) NOT NULL,
  overs_id varchar(255) NOT NULL,
  PRIMARY KEY (innings_id, overs_id),
  CONSTRAINT fk_innings_overs_innings FOREIGN KEY (innings_id) REFERENCES innings (id) ON DELETE CASCADE,
  CONSTRAINT fk_innings_overs_over FOREIGN KEY (overs_id) REFERENCES "over" (id) ON DELETE CASCADE
);

CREATE TABLE over_balls (
  over_id varchar(255) NOT NULL,
  balls_id varchar(255) NOT NULL,
  PRIMARY KEY (over_id, balls_id),
  CONSTRAINT fk_over_balls_over FOREIGN KEY (over_id) REFERENCES "over" (id) ON DELETE CASCADE,
  CONSTRAINT fk_over_balls_ball FOREIGN KEY (balls_id) REFERENCES ball (id) ON DELETE CASCADE
);

CREATE TABLE team_players (
  team_id varchar(255) NOT NULL,
  players_id varchar(255) NOT NULL,
  PRIMARY KEY (team_id, players_id),
  CONSTRAINT fk_team_players_team FOREIGN KEY (team_id) REFERENCES team (id) ON DELETE CASCADE,
  CONSTRAINT fk_team_players_player FOREIGN KEY (players_id) REFERENCES player (id) ON DELETE CASCADE
);
