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
@Table(name = "fall_of_wickets")
public class FallOfWicket {

    @Id
    private String id;

    @Column(name = "match_id")
    private String matchId;

    @Column(name = "innings_id")
    private String inningsId;

    @Column(name = "innings_number")
    private int inningsNumber;

    @Column(name = "wicket_number")
    private int wicketNumber;

    @Column(name = "runs_at_fall")
    private int teamScoreAtFall;

    @Column(name = "over_at_fall")
    private int overNumber;

    @Column(name = "ball_number")
    private int ballNumber;

    @Column(name = "player_out_id")
    private String playerOutId;

    @Column(name = "bowler_id")
    private String bowlerId;

    @Column(name = "fielder_id")
    private String fielderId;

    @Column(name = "ball_id")
    private String ballId;
}
