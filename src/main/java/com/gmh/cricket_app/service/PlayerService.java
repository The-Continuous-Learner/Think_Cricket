package com.gmh.cricket_app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gmh.cricket_app.models.Player;
import com.gmh.cricket_app.repositories.PlayerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;

    public Player savePlayer(Player player) {
        Player saved = playerRepository.save(player);
        log.info("Player saved: playerId={}, name={}", saved.getId(), saved.getName());
        return saved;
    }

    public Optional<Player> getPlayerById(String id) {
        return playerRepository.findById(id);
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public List<Player> getPlayersByName(String name) {
        return playerRepository.findByName(name);
    }
}
