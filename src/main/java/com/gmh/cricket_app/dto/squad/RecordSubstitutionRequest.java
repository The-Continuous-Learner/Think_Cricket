package com.gmh.cricket_app.dto.squad;

import com.gmh.cricket_app.enums.SubstitutionType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecordSubstitutionRequest {
    private String sessionToken;
    private String matchId;
    private String teamId;
    private String playerOutId;
    private String playerInId;
    private int inningsNumber;
    private int overNumber;
    private SubstitutionType substitutionType;
}
