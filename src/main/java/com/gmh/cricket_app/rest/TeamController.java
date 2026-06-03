package com.gmh.cricket_app.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gmh.cricket_app.dto.team.AddPlayerToTeamRequest;
import com.gmh.cricket_app.dto.team.CreateTeamRequest;
import com.gmh.cricket_app.dto.team.DeleteTeamRequest;
import com.gmh.cricket_app.dto.team.ModifyTeamRequest;
import com.gmh.cricket_app.models.team.Team;
import com.gmh.cricket_app.service.TeamService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

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
}

