package com.gmh.cricket_app.cache;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.gmh.cricket_app.dto.innings.ScoreCardResponse;

@Component
public class CaffeineScorecardCache implements ScorecardCache {

    private final Cache<String, ScoreCardResponse> cache;

    public CaffeineScorecardCache(
            @Value("${cricket.cache.scorecard.max-size}") long maxSize,
            @Value("${cricket.cache.scorecard.ttl-days}") long ttlDays) {
        this.cache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(ttlDays, TimeUnit.DAYS)
                .build();
    }

    @Override
    public Optional<ScoreCardResponse> get(String matchId) {
        return Optional.ofNullable(cache.getIfPresent(matchId));
    }

    @Override
    public void put(String matchId, ScoreCardResponse scorecard) {
        cache.put(matchId, scorecard);
    }

    @Override
    public void evict(String matchId) {
        cache.invalidate(matchId);
    }
}
