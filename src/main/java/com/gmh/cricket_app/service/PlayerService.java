package com.gmh.cricket_app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gmh.cricket_app.dto.player.DeletePlayerRequest;
import com.gmh.cricket_app.dto.player.SavePlayerRequest;
import com.gmh.cricket_app.dto.player.UpdatePlayerRequest;
import com.gmh.cricket_app.exceptions.BadRequestException;
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
        String userId = sessionService.validateSession(sessionToken).getId();
        Player player = new Player();
        player.setName(req.getName());
        player.setAge(req.getAge());
        player.setGender(req.getGender());
        player.setType(req.getType());
        player.setCreatedByUserId(userId);
        Player saved = playerRepository.save(player);
        log.info("Player saved: playerId={}, name={}", saved.getId(), saved.getName());
        return saved;
    }

    public Optional<Player> getPlayerById(String sessionToken, String id) {
        sessionService.validateSession(sessionToken);
        return playerRepository.findById(id);
    }

    public List<Player> getAllPlayers(String sessionToken) {
        String userId = sessionService.validateSession(sessionToken).getId();
        return playerRepository.findByCreatedByUserId(userId);
    }

    public List<Player> getPlayersByName(String sessionToken, String name) {
        sessionService.validateSession(sessionToken);
        return playerRepository.findByName(name);
    }

    public Player updatePlayer(UpdatePlayerRequest req) {
        sessionService.validateSession(req.getSessionToken());
        Player player = playerRepository.findById(req.getPlayerId())
                .orElseThrow(() -> new BadRequestException("Player not found"));
        player.setName(req.getName());
        player.setAge(req.getAge());
        player.setGender(req.getGender());
        player.setType(req.getType());
        Player updated = playerRepository.save(player);
        log.info("Player updated: playerId={}, name={}", updated.getId(), updated.getName());
        return updated;
    }

    public void deletePlayer(DeletePlayerRequest req) {
        sessionService.validateSession(req.getSessionToken());
        Player player = playerRepository.findById(req.getPlayerId())
                .orElseThrow(() -> new BadRequestException("Player not found"));
        playerRepository.delete(player);
        log.info("Player deleted: playerId={}", req.getPlayerId());
    }
}
