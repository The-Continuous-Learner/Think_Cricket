package com.gmh.cricket_app.dto.team;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamPlayersRequest {
    private String sessionToken;
    private String teamId;
}
