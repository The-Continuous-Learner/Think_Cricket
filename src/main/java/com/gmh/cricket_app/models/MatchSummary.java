package com.gmh.cricket_app.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
public class MatchSummary {

    @Id
    private String id; // matchId + "-SUMMARY"

    private String matchId;

    private String winnerTeamId;
    private String loserTeamId;

    private String resultText; // "India won by 5 wickets", etc.

    private int teamARuns;
    private int teamAOvers;
    private int teamAWickets;

    private int teamBRuns;
    private int teamBOvers;
    private int teamBWickets;

    private String manOfTheMatchId;
}
