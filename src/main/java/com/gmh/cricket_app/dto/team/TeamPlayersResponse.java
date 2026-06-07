package com.gmh.cricket_app.dto.team;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeamPlayersResponse {
    private String teamId;
    private List<PlayerInfo> players;
}
