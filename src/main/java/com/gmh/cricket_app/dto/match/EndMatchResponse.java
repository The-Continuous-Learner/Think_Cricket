package com.gmh.cricket_app.dto.match;

import com.gmh.cricket_app.enums.MatchStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EndMatchResponse {
    private String matchId;
    private MatchStatus status;
    private Long actualEndTime;
    private String endedByUserId;
}


