package com.gmh.cricket_app.dto.match;

import com.gmh.cricket_app.enums.MatchStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EndMatchRequest {

    private String sessionToken;
    private String matchId;

    private MatchStatus finalStatus; 
    // COMPLETED, ABANDONED, CANCELLED
}

