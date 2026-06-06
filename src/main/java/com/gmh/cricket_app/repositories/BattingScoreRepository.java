package com.gmh.cricket_app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gmh.cricket_app.models.BattingScore;

public interface BattingScoreRepository extends JpaRepository<BattingScore, String> {

    Optional<BattingScore> findByInningsIdAndPlayerId(String inningsId, String playerId);

    List<BattingScore> findByInningsIdOrderByBattingPositionAsc(String inningsId);

    long countByInningsId(String inningsId);
}
