package com.gmh.cricket_app.dto.match;

import com.gmh.cricket_app.enums.MatchStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LiveStateResponse {
    private String matchId;
    private String format;
    private MatchStatus status;
    private int totalOvers;
    private String teamAId;
    private String teamAName;
    private String teamBId;
    private String teamBName;
    private ActiveInningsSummary activeInnings;
    private ActiveOverSummary activeOver;
    private String lastBatsmanId;
    private String lastBatsmanName;
    private String lastNonStrikerId;
    private String lastNonStrikerName;
}
