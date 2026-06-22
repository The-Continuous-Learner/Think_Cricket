package com.gmh.cricket_app.dto.squad;

import com.gmh.cricket_app.enums.PlayerRole;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SquadPlayerEntry {
    private String playerId;
    private String playerName;
    private PlayerRole role;
    private boolean captain;
    private boolean viceCaptain;
}
