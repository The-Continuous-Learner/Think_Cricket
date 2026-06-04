package com.gmh.cricket_app.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name = "batting_score")
public class BattingScore {

    @Id
    private String id;

    @Column(name = "match_id")
    private String matchId;

    @Column(name = "innings_id")
    private String inningsId;

    @Column(name = "innings_number")
    private int inningsNumber;

    @Column(name = "player_id")
    private String playerId;

    @Column(name = "batting_position")
    private int battingPosition;

    private int runs;
    private int balls;
    private int fours;
    private int sixes;

    private boolean out;

    @Column(name = "dismissal_type")
    private String dismissalType;

    @Column(name = "dismissal_ball_id")
    private String dismissalBallId;

    @Column(name = "strike_rate")
    private double strikeRate;
}
