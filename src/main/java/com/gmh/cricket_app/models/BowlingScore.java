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
public class BowlingScore {

    @Id
    private String id; // matchId-INN-<inningsNumber>-BOWL-<bowlerId>

    private String matchId;
    private String inningsId;
    private int inningsNumber;

    private String bowlerId;

    private int ballsBowled;      // count legal deliveries
    private int overs;            // derived from ballsBowled
    private int maidens;

    private int runsConceded;
    private int wickets;

    private double economy;
}
