package com.gmh.cricket_app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gmh.cricket_app.dto.team.AddPlayerToTeamRequest;
import com.gmh.cricket_app.dto.team.CreateTeamRequest;
import com.gmh.cricket_app.dto.team.DeleteTeamRequest;
import com.gmh.cricket_app.dto.team.ModifyTeamRequest;
import com.gmh.cricket_app.models.team.Team;
import com.gmh.cricket_app.models.team.TeamPlayerMapper;
import com.gmh.cricket_app.repositories.TeamPlayerMapperRepository;
import com.gmh.cricket_app.repositories.TeamRepository;
import com.gmh.cricket_app.util.CommonUtil;
import com.gmh.cricket_app.exceptions.BadRequestException;
import com.gmh.cricket_app.repositories.PlayerRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class TeamService {

    private final TeamRepository teamRepo;
    private final SessionService sessionService;
    private final TeamPlayerMapperRepository mapperRepo;
    private final PlayerRepository playerRepo;

    @Value("${cricket.team.id-length}")
    private int teamIdLength;

    public Team createTeam(CreateTeamRequest req) {

        String userId = sessionService.validateSession(req.getSessionToken()).getId();

        if (teamRepo.existsByName(req.getName())) {
            log.warn("Create team failed - name already exists: {}", req.getName());
            throw new BadRequestException("Team name already exists");
        }

        Team team = new Team();
        team.setId(CommonUtil.generateId(teamIdLength));
        team.setName(req.getName());
        team.setDescription(req.getDescription());
        team.setCreatedByUserId(userId);

        teamRepo.save(team);

        log.info("Team created: teamId={}, name={}", team.getId(), team.getName());
        return team;
    }

    public void modifyTeam(ModifyTeamRequest req) {

        sessionService.validateSession(req.getSessionToken());

        Team team = teamRepo.findById(req.getTeamId())
                .orElseThrow(() -> new BadRequestException("Team not found"));

        if (req.getName() != null && !req.getName().equals(team.getName())) {
            if (teamRepo.existsByName(req.getName())) {
                log.warn("Modify team failed - name already exists: {}", req.getName());
                throw new BadRequestException("Team name already exists");
            }
            team.setName(req.getName());
        }

        if (req.getDescription() != null) {
            team.setDescription(req.getDescription());
        }

        teamRepo.save(team);

        log.info("Team modified: teamId={}", team.getId());
    }

    @Transactional
    public void deleteTeam(DeleteTeamRequest req) {

        sessionService.validateSession(req.getSessionToken());

        if (!teamRepo.existsById(req.getTeamId())) {
            throw new BadRequestException("Team not found");
        }

        mapperRepo.deleteByTeamId(req.getTeamId());
        teamRepo.deleteById(req.getTeamId());

        log.info("Team deleted: teamId={}", req.getTeamId());
    }

    public void addPlayerToTeam(AddPlayerToTeamRequest req) {

        sessionService.validateSession(req.getSessionToken());

        if (!teamRepo.existsById(req.getTeamId())) {
            throw new BadRequestException("Team not found");
        }
        if (!playerRepo.existsById(req.getPlayerId())) {
            throw new BadRequestException("Player not found");
        }
        if (mapperRepo.existsByTeamIdAndPlayerId(req.getTeamId(), req.getPlayerId())) {
            log.warn("Add player failed - already in team: teamId={}, playerId={}", req.getTeamId(), req.getPlayerId());
            throw new BadRequestException("Player already in team");
        }

        mapperRepo.save(new TeamPlayerMapper(req.getTeamId(), req.getPlayerId()));

        log.info("Player added to team: teamId={}, playerId={}", req.getTeamId(), req.getPlayerId());
    }

    public List<Team> getMyTeams(String sessionToken) {
        String userId = sessionService.validateSession(sessionToken).getId();
        return teamRepo.findByCreatedByUserId(userId);
    }

    @Transactional
    public void removePlayerFromTeam(String sessionToken, String teamId, String playerId) {

        sessionService.validateSession(sessionToken);

        if (!mapperRepo.existsByTeamIdAndPlayerId(teamId, playerId)) {
            log.warn("Remove player failed - not in team: teamId={}, playerId={}", teamId, playerId);
            throw new BadRequestException("Player not in team");
        }

        mapperRepo.deleteByTeamIdAndPlayerId(teamId, playerId);

        log.info("Player removed from team: teamId={}, playerId={}", teamId, playerId);
    }
}
