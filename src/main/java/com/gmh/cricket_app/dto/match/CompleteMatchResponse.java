package com.gmh.cricket_app.dto.match;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CompleteMatchResponse {

    private String matchId;
    private String winnerTeamId;
    private String loserTeamId;
    private boolean draw;
    private String resultText;
    private boolean decidedBySuperOver;
}
