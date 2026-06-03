package com.gmh.cricket_app.dto.match;

import com.gmh.cricket_app.enums.MatchStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StartMatchResponse {
    private String matchId;
    private MatchStatus status; // IN_PROGRESS
    private long actualStartTime;
    private String startedByUserId;
}