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
@Table(name = "bowling_score")
public class BowlingScore {

    @Id
    private String id;

    @Column(name = "match_id")
    private String matchId;

    @Column(name = "innings_id")
    private String inningsId;

    @Column(name = "innings_number")
    private int inningsNumber;

    @Column(name = "player_id")
    private String bowlerId;

    @Column(name = "balls_bowled")
    private int ballsBowled;

    private int overs;
    private int maidens;

    @Column(name = "runs")
    private int runsConceded;

    private int wickets;
    private double economy;
}
