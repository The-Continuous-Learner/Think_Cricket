package com.gmh.cricket_app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gmh.cricket_app.models.MatchSubstitution;

public interface MatchSubstitutionRepository extends JpaRepository<MatchSubstitution, String> {

    List<MatchSubstitution> findByMatchIdAndTeamIdOrderByInningsNumberAscOverNumberAsc(String matchId, String teamId);
}
