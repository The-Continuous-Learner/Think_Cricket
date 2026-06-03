package com.gmh.cricket_app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gmh.cricket_app.models.team.TeamPlayerId;
import com.gmh.cricket_app.models.team.TeamPlayerMapper;

public interface TeamPlayerMapperRepository extends JpaRepository<TeamPlayerMapper, TeamPlayerId> {

    boolean existsByTeamIdAndPlayerId(String teamId, String playerId);

    List<TeamPlayerMapper> findByTeamId(String teamId);

    void deleteByTeamId(String teamId);
}

