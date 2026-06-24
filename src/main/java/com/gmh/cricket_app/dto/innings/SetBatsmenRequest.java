package com.gmh.cricket_app.dto.innings;

import lombok.Getter;

@Getter
public class SetBatsmenRequest {
    private String sessionToken;
    private String inningsId;
    private String strikerId;
    private String nonStrikerId;
}
