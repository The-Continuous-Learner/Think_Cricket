package com.gmh.cricket_app.dto.over;

import com.gmh.cricket_app.enums.BoundaryType;
import com.gmh.cricket_app.enums.ExtraType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BallInfo {
    private String ballId;
    private int ballNumber;
    private boolean legalDelivery;
    private int runs;
    private int extraRuns;
    private ExtraType extraType;
    private BoundaryType boundaryType;
    private String batsmanId;
    private String batsmanName;
    private String nonStrikerId;
    private String nonStrikerName;
    private String bowlerId;
    private String bowlerName;
    private boolean wicket;
}
