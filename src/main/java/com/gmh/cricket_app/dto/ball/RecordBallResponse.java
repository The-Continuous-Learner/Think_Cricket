package com.gmh.cricket_app.dto.ball;

import com.gmh.cricket_app.enums.BoundaryType;
import com.gmh.cricket_app.enums.ExtraType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecordBallResponse {

    private String ballId;
    private String overId;
    private String inningsId;
    private int ballNumber;
    private boolean legalDelivery;
    private int runs;
    private int extraRuns;
    private ExtraType extraType;
    private BoundaryType boundaryType;
    private boolean wicket;
    private int legalBallsInOver;
    private boolean overCompleted;
    private boolean inningsCompleted;
}
