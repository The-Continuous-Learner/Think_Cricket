package com.gmh.cricket_app.dto.over;

import com.gmh.cricket_app.enums.OverStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EndOverResponse {

    private String overId;
    private String inningsId;
    private int overNumber;
    private String bowlerId;
    private int totalRuns;
    private int wickets;
    private OverStatus status;
    private int inningsOversCompleted;
}
