package com.gmh.cricket_app.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class InningsScoreCard {

    private String inningsId;
    private int inningsNumber;

    private String battingTeamId;
    private String bowlingTeamId;

    private List<BattingScore> batting;
    private List<BowlingScore> bowling;
    private List<FallOfWicket> fallOfWickets;

    private int totalRuns;
    private int totalWickets;
    private int totalOvers;
}
