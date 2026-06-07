package com.gmh.cricket_app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gmh.cricket_app.dto.team.PlayerInfo;
import com.gmh.cricket_app.dto.team.TeamPlayersResponse;
import com.gmh.cricket_app.models.team.TeamPlayerMapper;
import com.gmh.cricket_app.repositories.PlayerRepository;
import com.gmh.cricket_app.repositories.TeamPlayerMapperRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamPlayersService {

    private final TeamPlayerMapperRepository teamPlayerMapperRepo;
    private final PlayerRepository playerRepo;
    private final SessionService sessionService;

    public TeamPlayersResponse getTeamPlayers(String sessionToken, String teamId) {
        sessionService.validateSession(sessionToken);

        List<String> playerIds = teamPlayerMapperRepo.findByTeamId(teamId)
                .stream()
                .map(TeamPlayerMapper::getPlayerId)
                .collect(Collectors.toList());

        List<PlayerInfo> players = playerRepo.findAllById(playerIds)
                .stream()
                .map(p -> new PlayerInfo(p.getId(), p.getName(), p.getType()))
                .collect(Collectors.toList());

        return new TeamPlayersResponse(teamId, players);
    }
}
