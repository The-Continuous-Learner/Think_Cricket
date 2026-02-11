package com.gmh.cricket_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gmh.cricket_app.models.Player;

public interface PlayerRepository extends JpaRepository<Player, String> {
    
}
