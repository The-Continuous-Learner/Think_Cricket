package com.gmh.cricket_app.dto.score;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FallOfWicketLine {

    private int wicketNumber;
    private int teamScoreAtFall;
    private int overNumber;
    private int ballNumber;
    private String playerOutId;
    private String bowlerId;
    private String fielderId;
}
