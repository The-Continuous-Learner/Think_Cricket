package com.gmh.cricket_app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gmh.cricket_app.dto.player.SavePlayerRequest;
import com.gmh.cricket_app.models.Player;
import com.gmh.cricket_app.repositories.PlayerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final SessionService sessionService;

    public Player savePlayer(String sessionToken, SavePlayerRequest req) {
        sessionService.validateSession(sessionToken);
        Player player = new Player();
        player.setName(req.getName());
        player.setAge(req.getAge());
        player.setGender(req.getGender());
        player.setType(req.getType());
        Player saved = playerRepository.save(player);
        log.info("Player saved: playerId={}, name={}", saved.getId(), saved.getName());
        return saved;
    }

    public Optional<Player> getPlayerById(String sessionToken, String id) {
        sessionService.validateSession(sessionToken);
        return playerRepository.findById(id);
    }

    public List<Player> getAllPlayers(String sessionToken) {
        sessionService.validateSession(sessionToken);
        return playerRepository.findAll();
    }

    public List<Player> getPlayersByName(String sessionToken, String name) {
        sessionService.validateSession(sessionToken);
        return playerRepository.findByName(name);
    }
}
