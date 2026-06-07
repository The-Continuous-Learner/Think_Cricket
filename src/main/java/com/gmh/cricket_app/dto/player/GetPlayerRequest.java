package com.gmh.cricket_app.dto.player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPlayerRequest {
    private String sessionToken;
    private String playerId;
}
