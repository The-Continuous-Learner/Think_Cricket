package com.gmh.cricket_app.dto.score;

import java.util.List;

import com.gmh.cricket_app.enums.InningsStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InningsSummary {

    private int inningsNumber;
    private String battingTeamId;
    private String battingTeamName;
    private String bowlingTeamId;
    private String bowlingTeamName;
    private InningsStatus status;
    private int totalRuns;
    private int wickets;
    private int oversCompleted;
    private int extras;
    private List<BattingLine> batting;
    private List<BowlingLine> bowling;
    private List<FallOfWicketLine> fallOfWickets;
}
