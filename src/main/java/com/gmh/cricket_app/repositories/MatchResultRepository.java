package com.gmh.cricket_app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gmh.cricket_app.models.MatchResult;

public interface MatchResultRepository extends JpaRepository<MatchResult, String> {

    Optional<MatchResult> findByMatchId(String matchId);
}
