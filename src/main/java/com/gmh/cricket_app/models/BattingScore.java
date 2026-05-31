package com.gmh.cricket_app.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
public class BattingScore {

    @Id
    private String id; // matchId-INN-<inningsNumber>-BAT-<playerId>

    private String matchId;
    private String inningsId;
    private int inningsNumber;

    private String playerId;
    private int battingPosition;

    private int runs;
    private int balls;
    private int fours;
    private int sixes;

    private boolean out;
    private String dismissalType;   // "CAUGHT", "BOWLED", etc.
    private String dismissalBallId; // hierarchical ball ID

    private double strikeRate;
}
