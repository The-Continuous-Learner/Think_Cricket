package com.gmh.cricket_app.dto.innings;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EndInningsRequest {

    private String sessionToken;
    private String inningsId;
}
