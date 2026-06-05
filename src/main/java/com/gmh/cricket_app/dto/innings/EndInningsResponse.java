package com.gmh.cricket_app.dto.innings;

import com.gmh.cricket_app.enums.InningsStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EndInningsResponse {

    private String inningsId;
    private String matchId;
    private int inningsNumber;
    private int totalRuns;
    private int wickets;
    private int extras;
    private int oversCompleted;
    private InningsStatus status;
}
