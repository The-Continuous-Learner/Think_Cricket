package com.gmh.cricket_app.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Innings {

    @Id
    private String id; // matchId-INN-1

    private String matchId;
    private int inningsNumber;

    @ManyToOne
    private Team battingTeam;

    @ManyToOne
    private Team bowlingTeam;

    @OneToMany
    private List<Over> overs = new ArrayList<>();

    private int totalRuns;
    private int wickets;
    private int extras;

    private Integer target; // only for second innings
}

