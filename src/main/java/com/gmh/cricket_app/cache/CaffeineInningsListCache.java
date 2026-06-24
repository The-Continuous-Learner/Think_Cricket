package com.gmh.cricket_app.cache;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.gmh.cricket_app.dto.innings.InningsSummary;

@Component
public class CaffeineInningsListCache implements InningsListCache {

    private final Cache<String, List<InningsSummary>> cache;

    public CaffeineInningsListCache(
            @Value("${cricket.cache.innings-list.max-size}") long maxSize,
            @Value("${cricket.cache.innings-list.ttl-seconds}") long ttlSeconds) {
        this.cache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(ttlSeconds, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public Optional<List<InningsSummary>> get(String matchId) {
        return Optional.ofNullable(cache.getIfPresent(matchId));
    }

    @Override
    public void put(String matchId, List<InningsSummary> innings) {
        cache.put(matchId, innings);
    }

    @Override
    public void evict(String matchId) {
        cache.invalidate(matchId);
    }
}
