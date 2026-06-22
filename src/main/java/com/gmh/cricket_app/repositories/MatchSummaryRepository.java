package com.gmh.cricket_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gmh.cricket_app.models.MatchSummary;

public interface MatchSummaryRepository extends JpaRepository<MatchSummary, String> {

    void deleteByMatchId(String matchId);
}
