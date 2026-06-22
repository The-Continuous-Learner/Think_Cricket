package com.gmh.cricket_app.dto.innings;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetInningsListRequest {

    private String matchId;
    private String sessionToken;
}
