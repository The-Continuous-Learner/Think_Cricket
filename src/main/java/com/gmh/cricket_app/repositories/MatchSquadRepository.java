package com.gmh.cricket_app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gmh.cricket_app.models.MatchSquad;
import com.gmh.cricket_app.models.MatchSquadId;

public interface MatchSquadRepository extends JpaRepository<MatchSquad, MatchSquadId> {

    List<MatchSquad> findByMatchIdAndTeamId(String matchId, String teamId);

    boolean existsByMatchIdAndTeamId(String matchId, String teamId);

    void deleteByMatchIdAndTeamId(String matchId, String teamId);
}
