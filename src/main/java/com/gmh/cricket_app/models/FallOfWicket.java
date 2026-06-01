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
public class FallOfWicket {

    @Id
    private String id; // matchId-INN-<inningsNumber>-FOW-<wicketNumber>

    private String matchId;
    private String inningsId;
    private int inningsNumber;

    private int wicketNumber;     // 1, 2, 3, ...
    private int teamScoreAtFall;  // runs at the moment of wicket
    private int overNumber;
    private int ballNumber;

    private String playerOutId;
    private String bowlerId;
    private String fielderId;

    private String ballId; // hierarchical ball ID
}
