package com.gmh.cricket_app.dto.score;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BattingLine {

    private String playerId;
    private String playerName;
    private int battingPosition;
    private int runs;
    private int balls;
    private int fours;
    private int sixes;
    private double strikeRate;
    private boolean out;
    private String dismissalType;
    private String dismissalBallId;
}
