package com.gmh.cricket_app.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name = "match_summary")
public class MatchSummary {

    @Id
    private String id;

    @Column(name = "match_id")
    private String matchId;

    @Column(name = "winner_id")
    private String winnerTeamId;

    @Column(name = "loser_team_id")
    private String loserTeamId;

    @Column(name = "result_text")
    private String resultText;

    @Column(name = "team_a_runs")
    private int teamARuns;

    @Column(name = "team_a_overs")
    private int teamAOvers;

    @Column(name = "team_a_wickets")
    private int teamAWickets;

    @Column(name = "team_b_runs")
    private int teamBRuns;

    @Column(name = "team_b_overs")
    private int teamBOvers;

    @Column(name = "team_b_wickets")
    private int teamBWickets;

    @Column(name = "player_of_match_id")
    private String manOfTheMatchId;
}
