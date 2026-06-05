package com.gmh.cricket_app.dto.over;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StartOverRequest {

    private String sessionToken;
    private String inningsId;
    private String bowlerId;
}
