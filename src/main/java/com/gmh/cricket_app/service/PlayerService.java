package com.gmh.cricket_app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gmh.cricket_app.models.Player;
import com.gmh.cricket_app.repositories.PlayerRepository;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }

    public Optional<Player> getPlayerById(String id) {
        return playerRepository.findById(id);
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

}

