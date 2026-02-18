package com.gmh.cricket_app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gmh.cricket_app.models.Player;

public interface PlayerRepository extends JpaRepository<Player, String> {
    public List<Player> findByName(String name);
}
