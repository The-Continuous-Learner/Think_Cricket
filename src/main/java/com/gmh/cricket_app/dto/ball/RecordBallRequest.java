package com.gmh.cricket_app.dto.ball;

import com.gmh.cricket_app.enums.BoundaryType;
import com.gmh.cricket_app.enums.ExtraType;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecordBallRequest {

    @NotBlank
    private String sessionToken;

    @NotBlank
    private String overId;

    @NotBlank
    private String batsmanId;

    @NotBlank
    private String nonStrikerId;

    @Min(0) @Max(6)
    private int runs;           // runs off the bat

    @Min(0)
    private int extraRuns;      // wide/no-ball penalty + bye/leg-bye runs

    private ExtraType extraType;       // null for normal delivery
    private BoundaryType boundaryType; // null if not a boundary

    private String bowlerId;            // optional override; defaults to over's assigned bowler

    private boolean wicket;
}
