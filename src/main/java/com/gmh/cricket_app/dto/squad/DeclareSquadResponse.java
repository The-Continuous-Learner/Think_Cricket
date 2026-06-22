package com.gmh.cricket_app.dto.squad;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeclareSquadResponse {
    private String matchId;
    private String teamId;
    private List<SquadPlayerEntry> players;
}
