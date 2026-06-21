package com.gmh.cricket_app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gmh.cricket_app.models.team.Team;

public interface TeamRepository extends JpaRepository<Team, String> {

    boolean existsByName(String name);
    List<Team> findByCreatedByUserId(String userId);
}

