package com.gmh.cricket_app.dto.match;

import com.gmh.cricket_app.enums.OverStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ActiveOverSummary {
    private String overId;
    private int overNumber;
    private String bowlerId;
    private OverStatus status;
    private int legalBallCount;
    private int totalRuns;
    private int wickets;
}
