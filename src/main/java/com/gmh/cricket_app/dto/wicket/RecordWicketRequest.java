package com.gmh.cricket_app.dto.wicket;

import com.gmh.cricket_app.enums.WicketType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecordWicketRequest {

    @NotBlank
    private String sessionToken;

    @NotBlank
    private String ballId;

    @NotBlank
    private String playerOutId;

    @NotNull
    private WicketType type;

    private String bowlerId;   // null for run-out, obstructing field, etc.
    private String fielderId;  // null if no fielder involved
}
