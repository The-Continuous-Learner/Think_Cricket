package com.gmh.cricket_app.dto.ball;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UndoBallResponse {
    private String undoneBallId;
    private String inningsId;
    private boolean overReopened;
    private boolean inningsReopened;
    private boolean wicketReversed;
}
