package com.gmh.cricket_app.dto.squad;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetSquadRequest {
    private String sessionToken;
    private String matchId;
    private String teamId;
}
