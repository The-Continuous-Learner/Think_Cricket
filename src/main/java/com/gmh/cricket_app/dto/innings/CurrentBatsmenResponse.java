package com.gmh.cricket_app.dto.innings;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CurrentBatsmenResponse {
    private String inningsId;
    private String strikerId;
    private String strikerName;
    private String nonStrikerId;
    private String nonStrikerName;
}
