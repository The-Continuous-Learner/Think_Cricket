package com.gmh.cricket_app.dto.over;

import java.util.List;

import com.gmh.cricket_app.enums.OverStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OverBallsResponse {
    private String overId;
    private int overNumber;
    private String inningsId;
    private String bowlerId;
    private OverStatus status;
    private int legalBallCount;
    private int totalRuns;
    private int wickets;
    private List<BallInfo> balls;
}
