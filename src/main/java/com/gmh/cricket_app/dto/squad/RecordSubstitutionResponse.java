package com.gmh.cricket_app.dto.squad;

import com.gmh.cricket_app.enums.SubstitutionType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecordSubstitutionResponse {
    private String id;
    private String matchId;
    private String teamId;
    private String playerOutId;
    private String playerOutName;
    private String playerInId;
    private String playerInName;
    private int inningsNumber;
    private int overNumber;
    private SubstitutionType substitutionType;
}
