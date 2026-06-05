package com.gmh.cricket_app.dto.innings;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ScoreCardResponse {

    private String matchId;
    private String matchStatus;
    private String resultText;

    private List<InningsScoreCard> innings;
}
