package com.gmh.cricket_app.dto.match;

import com.gmh.cricket_app.enums.InningsStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ActiveInningsSummary {
    private String inningsId;
    private int inningsNumber;
    private String battingTeamId;
    private String bowlingTeamId;
    private InningsStatus status;
    private int totalRuns;
    private int wickets;
    private int oversCompleted;
    private int extras;
    private Integer target; // null for 1st innings; first innings score + 1 for 2nd innings
}
