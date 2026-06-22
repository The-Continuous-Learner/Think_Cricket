package com.gmh.cricket_app.dto.squad;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeclareSquadRequest {
    private String sessionToken;
    private String matchId;
    private String teamId;
    private List<SquadPlayerRequest> players;
}
