package com.gmh.cricket_app.models;

import java.util.ArrayList;
import java.util.List;

import com.gmh.cricket_app.enums.InningsStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "innings")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Innings {

    @Id
    private String id;

    @Column(name = "match_id")
    private String matchId;

    @Column(name = "innings_number")
    private int inningsNumber;

    @Column(name = "batting_team_id")
    private String battingTeamId;

    @Column(name = "bowling_team_id")
    private String bowlingTeamId;

    @OneToMany
    @JoinTable(
        name = "innings_overs",
        joinColumns = @JoinColumn(name = "innings_id"),
        inverseJoinColumns = @JoinColumn(name = "overs_id")
    )
    private List<Over> overs = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private InningsStatus status;

    @Column(name = "overs_completed")
    private int oversCompleted;

    @Column(name = "total_runs")
    private int totalRuns;

    private int wickets;
    private int extras;
}
