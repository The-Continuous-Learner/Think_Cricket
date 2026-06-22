package com.gmh.cricket_app.models;

import com.gmh.cricket_app.enums.PlayerRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "match_squad")
@IdClass(MatchSquadId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchSquad {

    @Id
    @Column(name = "match_id")
    private String matchId;

    @Id
    @Column(name = "team_id")
    private String teamId;

    @Id
    @Column(name = "player_id")
    private String playerId;

    @Enumerated(EnumType.STRING)
    private PlayerRole role;

    @Column(name = "is_captain", nullable = false)
    private boolean captain;

    @Column(name = "is_vice_captain", nullable = false)
    private boolean viceCaptain;
}
