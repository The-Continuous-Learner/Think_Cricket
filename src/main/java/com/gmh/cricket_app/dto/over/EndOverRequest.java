package com.gmh.cricket_app.dto.over;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EndOverRequest {

    private String sessionToken;
    private String overId;
}
