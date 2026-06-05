package com.gmh.cricket_app.models;

import java.util.ArrayList;
import java.util.List;

import com.gmh.cricket_app.enums.OverStatus;

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
@Table(name = "over")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Over {

    @Id
    private String id; // matchId-INN-inningsNumber-OVR-overNumber

    @Column(name = "match_id")
    private String matchId;

    @Column(name = "innings_id")
    private String inningsId;

    @Column(name = "innings_number")
    private int inningsNumber;

    @Column(name = "over_number")
    private int overNumber;

    @Column(name = "bowler_id")
    private String bowlerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OverStatus status;

    @Column(name = "total_runs")
    private int totalRuns;

    private int wickets;

    @OneToMany
    @JoinTable(
        name = "over_balls",
        joinColumns = @JoinColumn(name = "over_id"),
        inverseJoinColumns = @JoinColumn(name = "balls_id")
    )
    private List<Ball> balls = new ArrayList<>();
}
