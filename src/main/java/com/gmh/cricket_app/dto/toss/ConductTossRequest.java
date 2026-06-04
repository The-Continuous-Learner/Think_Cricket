package com.gmh.cricket_app.dto.toss;

import com.gmh.cricket_app.enums.TossDecision;
import com.gmh.cricket_app.enums.TossResult;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConductTossRequest {

    private String sessionToken;
    private String matchId;
    private TossResult tossResult;
    private String winnerTeamId;
    private TossDecision decision;
}
