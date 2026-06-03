package com.gmh.cricket_app.models.team;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter
@NoArgsConstructor 
@AllArgsConstructor
public class TeamPlayerId implements Serializable {
    private String teamId;
    private String playerId;
}

