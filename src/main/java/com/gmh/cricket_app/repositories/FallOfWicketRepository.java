package com.gmh.cricket_app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gmh.cricket_app.models.FallOfWicket;

public interface FallOfWicketRepository extends JpaRepository<FallOfWicket, String> {

    List<FallOfWicket> findByInningsIdOrderByWicketNumberAsc(String inningsId);

    long countByInningsId(String inningsId);

    Optional<FallOfWicket> findByBallId(String ballId);

    void deleteByMatchId(String matchId);
}
