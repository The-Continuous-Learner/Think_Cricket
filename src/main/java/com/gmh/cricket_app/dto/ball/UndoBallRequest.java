package com.gmh.cricket_app.dto.ball;

import lombok.Getter;

@Getter
public class UndoBallRequest {
    private String sessionToken;
    private String inningsId;
}
