package com.gmh.cricket_app.dto.score;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchScoreRequest {
    private String sessionToken;
    private String matchId;
}
