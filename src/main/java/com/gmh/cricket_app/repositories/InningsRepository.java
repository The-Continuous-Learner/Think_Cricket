package com.gmh.cricket_app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gmh.cricket_app.models.Innings;

public interface InningsRepository extends JpaRepository<Innings, String> {

    List<Innings> findByMatchIdOrderByInningsNumberAsc(String matchId);

    Optional<Innings> findByMatchIdAndInningsNumber(String matchId, int inningsNumber);
}
