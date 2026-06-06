package com.gmh.cricket_app.dto.ball;

import com.gmh.cricket_app.enums.BoundaryType;
import com.gmh.cricket_app.enums.ExtraType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecordBallRequest {

    private String sessionToken;
    private String overId;


    private String batsmanId;
    private String nonStrikerId;

    private int runs;           // runs off the bat
    private int extraRuns;      // wide/no-ball penalty + bye/leg-bye runs

    private ExtraType extraType;       // null for normal delivery
    private BoundaryType boundaryType; // null if not a boundary

    private String bowlerOverride;     // set only if bowler changed mid-over (injury)

    private boolean wicket;
}
