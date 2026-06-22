package com.gmh.cricket_app.dto.player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAllPlayersRequest {
    private String sessionToken;
    private int page = 0;
    private int size = 20;
}
