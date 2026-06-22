package com.gmh.cricket_app.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MatchSquadId implements Serializable {
    private String matchId;
    private String teamId;
    private String playerId;
}
