package com.gmh.cricket_app.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "match_result")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchResult {

    @Id
    private String id; // matchId + "-RESULT"

    @Column(name = "match_id", nullable = false)
    private String matchId;

    @Column(name = "winner_team_id")
    private String winnerTeamId; // null if draw

    @Column(name = "loser_team_id")
    private String loserTeamId; // null if draw

    @Column(name = "is_draw", nullable = false)
    private boolean draw;

    @Column(name = "result_text")
    private String resultText;

    @Column(name = "decided_by_super_over", nullable = false)
    private boolean decidedBySuperOver;
}
