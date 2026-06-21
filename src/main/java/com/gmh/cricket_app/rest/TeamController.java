package com.gmh.cricket_app.rest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gmh.cricket_app.dto.team.AddPlayerToTeamRequest;
import com.gmh.cricket_app.dto.team.CreateTeamRequest;
import com.gmh.cricket_app.dto.team.DeleteTeamRequest;
import com.gmh.cricket_app.dto.team.GetMyTeamsRequest;
import com.gmh.cricket_app.dto.team.ModifyTeamRequest;
import com.gmh.cricket_app.dto.team.RemovePlayerFromTeamRequest;
import com.gmh.cricket_app.dto.team.TeamPlayersRequest;
import com.gmh.cricket_app.dto.team.TeamPlayersResponse;
import com.gmh.cricket_app.models.team.Team;
import com.gmh.cricket_app.service.TeamPlayersService;
import com.gmh.cricket_app.service.TeamService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final TeamPlayersService teamPlayersService;

    @PostMapping("/create")
    public Team createTeam(@RequestBody CreateTeamRequest req) {
        return teamService.createTeam(req);
    }

    @PostMapping("/modify")
    public void modifyTeam(@RequestBody ModifyTeamRequest req) {
        teamService.modifyTeam(req);
    }

    @PostMapping("/delete")
    public void deleteTeam(@RequestBody DeleteTeamRequest req) {
        teamService.deleteTeam(req);
    }

    @PostMapping("/add-player")
    public void addPlayer(@RequestBody AddPlayerToTeamRequest req) {
        teamService.addPlayerToTeam(req);
    }

    @PostMapping("/remove-player")
    public void removePlayer(@RequestBody RemovePlayerFromTeamRequest req) {
        teamService.removePlayerFromTeam(req.getSessionToken(), req.getTeamId(), req.getPlayerId());
    }

    @GetMapping("/players")
    public TeamPlayersResponse getTeamPlayers(@RequestBody TeamPlayersRequest req) {
        return teamPlayersService.getTeamPlayers(req.getSessionToken(), req.getTeamId());
    }

    @GetMapping("/my")
    public List<Team> getMyTeams(@RequestBody GetMyTeamsRequest req) {
        return teamService.getMyTeams(req.getSessionToken());
    }
}
