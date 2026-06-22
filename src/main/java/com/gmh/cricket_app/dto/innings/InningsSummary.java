package com.gmh.cricket_app.dto.innings;

import com.gmh.cricket_app.enums.InningsStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InningsSummary {

    private String inningsId;
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
    private Integer target;
}
