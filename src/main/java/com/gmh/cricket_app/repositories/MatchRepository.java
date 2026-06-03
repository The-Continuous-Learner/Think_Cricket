package com.gmh.cricket_app.repositories;

import com.gmh.cricket_app.enums.MatchStatus;
import com.gmh.cricket_app.models.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, String> {

    // Fetch all matches hosted by a specific user
    List<Match> findByHostedByUserId(String hostedByUserId);

    // Optional: fetch matches by status (useful for dashboards or schedulers)
    List<Match> findByStatus(MatchStatus status);
}

