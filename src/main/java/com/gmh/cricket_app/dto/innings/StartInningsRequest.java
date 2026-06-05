package com.gmh.cricket_app.dto.innings;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StartInningsRequest {

    private String sessionToken;
    private String matchId;
    private String battingTeamId;
    private String bowlingTeamId;
}
