package com.gmh.cricket_app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gmh.cricket_app.models.Ball;

public interface BallRepository extends JpaRepository<Ball, String> {

    long countByOverId(String overId);

    List<Ball> findByOverIdOrderByBallNumberAsc(String overId);

    List<Ball> findByInningsIdOrderByOverNumberAscBallNumberAsc(String inningsId);

    Optional<Ball> findTopByInningsIdOrderByOverNumberDescBallNumberDesc(String inningsId);

    void deleteByMatchId(String matchId);
}
