package com.gmh.cricket_app.dto.wicket;

import com.gmh.cricket_app.enums.WicketType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecordWicketRequest {

    private String sessionToken;
    private String ballId;
    private String playerOutId;
    private WicketType type;
    private String bowlerId;   // null for run-out, obstructing field, etc.
    private String fielderId;  // null if no fielder involved
}
