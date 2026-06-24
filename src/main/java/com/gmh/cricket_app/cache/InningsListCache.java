package com.gmh.cricket_app.cache;

import java.util.List;
import java.util.Optional;

import com.gmh.cricket_app.dto.innings.InningsSummary;

public interface InningsListCache {

    Optional<List<InningsSummary>> get(String matchId);

    void put(String matchId, List<InningsSummary> innings);

    void evict(String matchId);
}
