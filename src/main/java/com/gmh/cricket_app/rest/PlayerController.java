package com.gmh.cricket_app.rest;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gmh.cricket_app.models.Player;
import com.gmh.cricket_app.service.PlayerService;

@RestController
@RequestMapping("/api/players")
public class PlayerController {
    
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping("/save-player")
    public Player savePlayer(@RequestBody Player player) {
        return playerService.savePlayer(player);
    }

    @GetMapping("/get-player/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable String id) {
        return playerService.getPlayerById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/get-all-players")
    public ResponseEntity<List<Player>> getAllPlayers() {
        return ResponseEntity.ok(playerService.getAllPlayers());
    }

    @GetMapping("/get-players-by-name/{name}")
    public ResponseEntity<List<Player>> getPlayersByName(@PathVariable String name) {
        List<Player> players = playerService.getPlayersByName(name);
        return players != null && !players.isEmpty() 
            ? ResponseEntity.ok(players) 
            : ResponseEntity.notFound().build();
    }

}
