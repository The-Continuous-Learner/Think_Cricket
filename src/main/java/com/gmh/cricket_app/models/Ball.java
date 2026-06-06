package com.gmh.cricket_app.models;

import com.gmh.cricket_app.enums.BoundaryType;
import com.gmh.cricket_app.enums.ExtraType;

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
import lombok.ToString;

@Entity
@Table(name = "ball")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Ball {

    @Id
    private String id; // overId-ballNumber e.g. matchId-INN-1-OVR-3-2

    @Column(name = "match_id")
    private String matchId;

    @Column(name = "innings_id")
    private String inningsId;

    @Column(name = "over_id")
    private String overId;

    @Column(name = "innings_number")
    private int inningsNumber;

    @Column(name = "over_number")
    private int overNumber;

    @Column(name = "ball_number")
    private int ballNumber;

    @Column(name = "legal_delivery")
    private boolean legalDelivery;

    private int runs;       // off the bat only

    @Column(name = "extra_runs")
    private int extraRuns;  // wide/no-ball penalty + bye/leg-bye runs

    @Enumerated(EnumType.STRING)
    @Column(name = "extra_type")
    private ExtraType extraType;

    @Enumerated(EnumType.STRING)
    @Column(name = "boundary_type")
    private BoundaryType boundaryType; // FOUR/SIX if boundary, null otherwise

    @Column(name = "bowler_id")
    private String bowlerId; // defaults to over bowler, overrideable for mid-over injury

    @Column(name = "batsman_id")
    private String batsmanId;

    @Column(name = "non_striker_id")
    private String nonStrikerId;

    private boolean wicket;
}
