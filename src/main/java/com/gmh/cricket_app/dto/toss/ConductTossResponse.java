package com.gmh.cricket_app.dto.toss;

import com.gmh.cricket_app.enums.TossDecision;
import com.gmh.cricket_app.enums.TossResult;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConductTossResponse {

    private String tossId;
    private String matchId;
    private TossResult tossResult;
    private String winnerTeamId;
    private String winnerTeamName;
    private String loserTeamId;
    private String loserTeamName;
    private TossDecision decision;
}
