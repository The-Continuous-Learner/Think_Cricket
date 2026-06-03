package com.gmh.cricket_app.models.team;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter 
@Setter
@NoArgsConstructor 
@AllArgsConstructor
@IdClass(TeamPlayerId.class)
@Table(name = "player_team_mapper")
public class TeamPlayerMapper {

    @Id
    @Column(name = "team_id")
    private String teamId;

    @Id
    @Column(name = "player_id")
    private String playerId;
}
