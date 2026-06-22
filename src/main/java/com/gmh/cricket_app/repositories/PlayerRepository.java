package com.gmh.cricket_app.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gmh.cricket_app.models.Player;

public interface PlayerRepository extends JpaRepository<Player, String> {
    List<Player> findByName(String name);
    List<Player> findByCreatedByUserId(String userId);
    Page<Player> findByCreatedByUserId(String userId, Pageable pageable);
}
