package com.gmh.cricket_app.dto.match;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HostMatchRequest {

    private String sessionToken;

    private String teamAId;
    private String teamBId;

    private String format;

    private Integer totalOvers; // 0 = unlimited (Test), null = not provided

    private long plannedStartTime; // epoch millis

    private String parentMatchId; // set only for super over matches
}