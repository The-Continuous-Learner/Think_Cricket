package com.gmh.cricket_app.dto.player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPlayersByNameRequest {
    private String sessionToken;
    private String name;
}
