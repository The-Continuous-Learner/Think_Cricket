package com.gmh.cricket_app.rest;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gmh.cricket_app.dto.player.DeletePlayerRequest;
import com.gmh.cricket_app.dto.player.GetAllPlayersRequest;
import com.gmh.cricket_app.dto.player.GetPlayerRequest;
import com.gmh.cricket_app.dto.player.GetPlayersByNameRequest;
import com.gmh.cricket_app.dto.player.SavePlayerRequest;
import com.gmh.cricket_app.dto.player.UpdatePlayerRequest;
import com.gmh.cricket_app.models.Player;
import com.gmh.cricket_app.service.PlayerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping("/save")
    public Player savePlayer(@RequestBody SavePlayerRequest req) {
        return playerService.savePlayer(req.getSessionToken(), req);
    }

    @GetMapping("/get")
    public ResponseEntity<Player> getPlayer(@RequestBody GetPlayerRequest req) {
        return playerService.getPlayerById(req.getSessionToken(), req.getPlayerId())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/get-all")
    public List<Player> getAllPlayers(@RequestBody GetAllPlayersRequest req) {
        return playerService.getAllPlayers(req.getSessionToken());
    }

    @GetMapping("/get-by-name")
    public ResponseEntity<List<Player>> getPlayersByName(@RequestBody GetPlayersByNameRequest req) {
        List<Player> players = playerService.getPlayersByName(req.getSessionToken(), req.getName());
        return players != null && !players.isEmpty()
                ? ResponseEntity.ok(players)
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/update")
    public Player updatePlayer(@RequestBody UpdatePlayerRequest req) {
        return playerService.updatePlayer(req);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deletePlayer(@RequestBody DeletePlayerRequest req) {
        playerService.deletePlayer(req);
        return ResponseEntity.noContent().build();
    }
}
