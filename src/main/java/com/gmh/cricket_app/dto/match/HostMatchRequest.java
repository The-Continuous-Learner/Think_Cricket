package com.gmh.cricket_app.dto.match;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HostMatchRequest {

    private String sessionToken;

    private String teamAId;
    private String teamBId;

    private String format; // T20, ODI, TEST

    private long plannedStartTime; // epoch millis
}