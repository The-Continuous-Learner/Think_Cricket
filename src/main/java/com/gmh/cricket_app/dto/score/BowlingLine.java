package com.gmh.cricket_app.dto.score;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BowlingLine {

    private String bowlerId;
    private int overs;
    private int ballsBowled;
    private int maidens;
    private int runsConceded;
    private int wickets;
    private double economy;
}
