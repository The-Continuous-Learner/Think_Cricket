package com.gmh.cricket_app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gmh.cricket_app.models.BowlingScore;

public interface BowlingScoreRepository extends JpaRepository<BowlingScore, String> {

    Optional<BowlingScore> findByInningsIdAndBowlerId(String inningsId, String bowlerId);

    List<BowlingScore> findByInningsId(String inningsId);

    void deleteByMatchId(String matchId);
}
