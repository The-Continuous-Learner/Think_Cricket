package com.gmh.cricket_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gmh.cricket_app.models.team.Team;

public interface TeamRepository extends JpaRepository<Team, String> {

    boolean existsByName(String name);
}

