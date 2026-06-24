package com.gmh.cricket_app.cache;

import java.util.Optional;

import com.gmh.cricket_app.dto.innings.ScoreCardResponse;

public interface ScorecardCache {

    Optional<ScoreCardResponse> get(String matchId);

    void put(String matchId, ScoreCardResponse scorecard);

    void evict(String matchId);
}
