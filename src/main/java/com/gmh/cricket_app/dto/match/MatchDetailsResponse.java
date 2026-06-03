package com.gmh.cricket_app.dto.match;

import com.gmh.cricket_app.enums.MatchStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MatchDetailsResponse {

    private String matchId;

    private String teamAId;
    private String teamBId;

    private String format;
    private MatchStatus status;

    private long plannedStartTime;
    private Long actualStartTime;
    private Long actualEndTime;

    private String hostedByUserId;
    private String startedByUserId;
    private String endedByUserId;
}