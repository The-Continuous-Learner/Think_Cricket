package com.gmh.cricket_app.dto.match;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CompleteMatchResponse {

    private String matchId;
    private String winnerTeamId;
    private String winnerTeamName;
    private String loserTeamId;
    private String loserTeamName;
    private boolean draw;
    private String resultText;
    private boolean decidedBySuperOver;
}
