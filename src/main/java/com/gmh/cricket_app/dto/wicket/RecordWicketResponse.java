package com.gmh.cricket_app.dto.wicket;

import com.gmh.cricket_app.enums.WicketType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecordWicketResponse {

    private String wicketId;
    private String ballId;
    private String inningsId;
    private String playerOutId;
    private WicketType type;
    private int wicketNumber;
    private int teamScoreAtFall;
}
