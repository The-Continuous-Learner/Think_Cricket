package com.gmh.cricket_app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gmh.cricket_app.enums.MatchStatus;
import com.gmh.cricket_app.models.Match;

public interface MatchRepository extends JpaRepository<Match, String> {

    List<Match> findByHostedByUserId(String hostedByUserId);

    List<Match> findByStatus(MatchStatus status);

    @Query(value = "SELECT EXISTS (" +
                   "SELECT 1 FROM matches WHERE status = 'IN_PROGRESS' " +
                   "AND (team_a_id IN :teams OR team_b_id IN :teams) LIMIT 1)",
           nativeQuery = true)
    boolean existsInProgressMatchInvolvingAnyOf(@Param("teams") List<String> teams);
}
