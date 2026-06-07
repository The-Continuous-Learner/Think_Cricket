package com.gmh.cricket_app.dto.score;

import java.util.List;

import com.gmh.cricket_app.enums.MatchStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MatchScoreResponse {

    private String matchId;
    private String format;
    private MatchStatus status;
    private int totalOvers;
    private String teamAId;
    private String teamBId;
    private List<InningsSummary> innings;
}
