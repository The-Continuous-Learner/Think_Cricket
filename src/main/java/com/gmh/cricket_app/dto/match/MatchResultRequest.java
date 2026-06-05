package com.gmh.cricket_app.dto.match;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchResultRequest {
    private String sessionToken;
    private String matchId;
}
