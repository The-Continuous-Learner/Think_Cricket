package com.gmh.cricket_app.dto.player;

import com.gmh.cricket_app.enums.Gender;
import com.gmh.cricket_app.enums.PlayerTypes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePlayerRequest {
    private String sessionToken;
    private String playerId;
    private String name;
    private int age;
    private Gender gender;
    private PlayerTypes type;
}
