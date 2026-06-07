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
    private String teamBId;
    private ActiveInningsSummary activeInnings; // null if no active innings
    private ActiveOverSummary activeOver;        // null if no active over
    private String lastBatsmanId;               // null if no balls bowled yet
    private String lastNonStrikerId;            // null if no balls bowled yet
}
