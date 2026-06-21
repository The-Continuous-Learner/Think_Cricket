package com.gmh.cricket_app.dto.match;

import com.gmh.cricket_app.enums.MatchStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MatchSummary {
    private String matchId;
    private String format;
    private MatchStatus status;
    private int totalOvers;
    private String teamAId;
    private String teamAName;
    private String teamBId;
    private String teamBName;
    private long plannedStartTime;
    private Long actualStartTime;
}
