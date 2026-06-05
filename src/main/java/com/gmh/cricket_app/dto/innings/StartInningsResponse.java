package com.gmh.cricket_app.dto.innings;

import com.gmh.cricket_app.enums.InningsStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StartInningsResponse {

    private String inningsId;
    private String matchId;
    private int inningsNumber;
    private String battingTeamId;
    private String bowlingTeamId;
    private Integer target; // null for innings 1, set for innings 2
    private InningsStatus status;
}
