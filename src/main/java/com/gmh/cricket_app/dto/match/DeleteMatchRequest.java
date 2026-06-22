package com.gmh.cricket_app.dto.match;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteMatchRequest {
    private String sessionToken;
    private String matchId;
}
