package com.gmh.cricket_app.dto.squad;

import com.gmh.cricket_app.enums.PlayerRole;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SquadPlayerRequest {
    private String playerId;
    private PlayerRole role;
}
