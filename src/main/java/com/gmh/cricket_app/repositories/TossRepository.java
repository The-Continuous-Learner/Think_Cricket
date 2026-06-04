package com.gmh.cricket_app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gmh.cricket_app.models.Toss;

public interface TossRepository extends JpaRepository<Toss, String> {

    Optional<Toss> findByMatchId(String matchId);

    boolean existsByMatchId(String matchId);
}
