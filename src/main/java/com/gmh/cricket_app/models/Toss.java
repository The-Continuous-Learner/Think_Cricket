package com.gmh.cricket_app.models;

import com.gmh.cricket_app.enums.TossDecision;
import com.gmh.cricket_app.enums.TossResult;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "toss")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Toss {

    @Id
    private String id; // matchId-TOSS

    @Column(name = "match_id", nullable = false)
    private String matchId;

    @Enumerated(EnumType.STRING)
    @Column(name = "toss_result", nullable = false)
    private TossResult tossResult;

    @Column(name = "winner_team_id", nullable = false)
    private String winnerTeamId;

    @Enumerated(EnumType.STRING)
    @Column(name = "decision", nullable = false)
    private TossDecision decision;
}
