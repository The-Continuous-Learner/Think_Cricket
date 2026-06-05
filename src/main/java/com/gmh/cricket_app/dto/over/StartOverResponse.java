package com.gmh.cricket_app.dto.over;

import com.gmh.cricket_app.enums.OverStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StartOverResponse {

    private String overId;
    private String inningsId;
    private String matchId;
    private int overNumber;
    private String bowlerId;
    private OverStatus status;
}
