package com.gmh.cricket_app.dto.team;

import com.gmh.cricket_app.enums.PlayerTypes;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlayerInfo {
    private String playerId;
    private String name;
    private PlayerTypes type;
}
