package com.gmh.cricket_app.service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gmh.cricket_app.dto.squad.DeclareSquadRequest;
import com.gmh.cricket_app.dto.squad.DeclareSquadResponse;
import com.gmh.cricket_app.dto.squad.GetSquadRequest;
import com.gmh.cricket_app.dto.squad.RecordSubstitutionRequest;
import com.gmh.cricket_app.dto.squad.RecordSubstitutionResponse;
import com.gmh.cricket_app.dto.squad.SquadPlayerEntry;
import com.gmh.cricket_app.enums.MatchStatus;
import com.gmh.cricket_app.enums.PlayerRole;
import com.gmh.cricket_app.exceptions.BadRequestException;
import com.gmh.cricket_app.models.Match;
import com.gmh.cricket_app.models.MatchSquad;
import com.gmh.cricket_app.models.MatchSubstitution;
import com.gmh.cricket_app.models.Player;
import com.gmh.cricket_app.repositories.MatchRepository;
import com.gmh.cricket_app.repositories.MatchSquadRepository;
import com.gmh.cricket_app.repositories.MatchSubstitutionRepository;
import com.gmh.cricket_app.repositories.PlayerRepository;
import com.gmh.cricket_app.repositories.TeamPlayerMapperRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchSquadService {

    @Value("${cricket.squad.playing-xi-size}")
    private int playingXiSize;

    @Value("${cricket.squad.max-size}")
    private int maxSquadSize;

    private final MatchSquadRepository matchSquadRepo;
    private final MatchSubstitutionRepository matchSubstitutionRepo;
    private final MatchRepository matchRepo;
    private final PlayerRepository playerRepo;
    private final TeamPlayerMapperRepository teamPlayerMapperRepo;
    private final SessionService sessionService;

    @Transactional
    public DeclareSquadResponse declareSquad(DeclareSquadRequest req) {
        sessionService.validateSession(req.getSessionToken());

        Match match = matchRepo.findById(req.getMatchId())
                .orElseThrow(() -> new BadRequestException("Match not found"));

        if (match.getStatus() != MatchStatus.IN_PROGRESS) {
            throw new BadRequestException("Squad can only be declared for an IN_PROGRESS match");
        }

        if (!req.getTeamId().equals(match.getTeamAId()) && !req.getTeamId().equals(match.getTeamBId())) {
            throw new BadRequestException("Team does not belong to this match");
        }

        List<String> playerIds = req.getPlayers().stream()
                .map(p -> p.getPlayerId())
                .collect(Collectors.toList());

        for (String playerId : playerIds) {
            if (!teamPlayerMapperRepo.existsByTeamIdAndPlayerId(req.getTeamId(), playerId)) {
                throw new BadRequestException("Player " + playerId + " does not belong to team " + req.getTeamId());
            }
        }

        if (req.getPlayers().size() > maxSquadSize) {
            throw new BadRequestException("Squad cannot exceed " + maxSquadSize + " players");
        }

        long playingCount = req.getPlayers().stream().filter(p -> p.getRole() == PlayerRole.PLAYING).count();
        if (playingCount != playingXiSize) {
            throw new BadRequestException("Exactly " + playingXiSize + " players must have role PLAYING, got " + playingCount);
        }

        long captainCount = req.getPlayers().stream().filter(p -> p.isCaptain()).count();
        long viceCaptainCount = req.getPlayers().stream().filter(p -> p.isViceCaptain()).count();

        if (captainCount != 1) {
            throw new BadRequestException("Exactly one captain must be declared");
        }
        if (viceCaptainCount > 1) {
            throw new BadRequestException("At most one vice captain can be declared");
        }

        boolean captainAndVcAreSame = req.getPlayers().stream()
                .anyMatch(p -> p.isCaptain() && p.isViceCaptain());
        if (captainAndVcAreSame) {
            throw new BadRequestException("Captain and vice captain must be different players");
        }

        matchSquadRepo.deleteByMatchIdAndTeamId(req.getMatchId(), req.getTeamId());

        List<MatchSquad> squad = req.getPlayers().stream()
                .map(p -> new MatchSquad(req.getMatchId(), req.getTeamId(), p.getPlayerId(), p.getRole(), p.isCaptain(), p.isViceCaptain()))
                .collect(Collectors.toList());

        matchSquadRepo.saveAll(squad);

        log.info("Squad declared: matchId={}, teamId={}, size={}", req.getMatchId(), req.getTeamId(), squad.size());

        Map<String, String> playerNames = playerRepo.findAllById(playerIds).stream()
                .collect(Collectors.toMap(Player::getId, Player::getName));

        List<SquadPlayerEntry> entries = req.getPlayers().stream()
                .map(p -> new SquadPlayerEntry(p.getPlayerId(), playerNames.get(p.getPlayerId()), p.getRole(), p.isCaptain(), p.isViceCaptain()))
                .collect(Collectors.toList());

        return new DeclareSquadResponse(req.getMatchId(), req.getTeamId(), entries);
    }

    public RecordSubstitutionResponse recordSubstitution(RecordSubstitutionRequest req) {
        sessionService.validateSession(req.getSessionToken());

        Match match = matchRepo.findById(req.getMatchId())
                .orElseThrow(() -> new BadRequestException("Match not found"));

        if (match.getStatus() != MatchStatus.IN_PROGRESS) {
            throw new BadRequestException("Match is not in progress");
        }

        if (!req.getTeamId().equals(match.getTeamAId()) && !req.getTeamId().equals(match.getTeamBId())) {
            throw new BadRequestException("Team does not belong to this match");
        }

        List<MatchSquad> squad = matchSquadRepo.findByMatchIdAndTeamId(req.getMatchId(), req.getTeamId());
        if (squad.isEmpty()) {
            throw new BadRequestException("Squad not declared for this team");
        }

        boolean playerOutInSquad = squad.stream().anyMatch(s -> s.getPlayerId().equals(req.getPlayerOutId()));
        if (!playerOutInSquad) {
            throw new BadRequestException("Player out is not in the declared squad");
        }

        boolean playerInInSquad = squad.stream().anyMatch(s -> s.getPlayerId().equals(req.getPlayerInId()));
        if (!playerInInSquad) {
            throw new BadRequestException("Player in is not in the declared squad");
        }

        MatchSubstitution sub = new MatchSubstitution(
                null,
                req.getMatchId(),
                req.getTeamId(),
                req.getPlayerOutId(),
                req.getPlayerInId(),
                req.getInningsNumber(),
                req.getOverNumber(),
                req.getSubstitutionType()
        );

        matchSubstitutionRepo.save(sub);

        log.info("Substitution recorded: matchId={}, teamId={}, out={}, in={}, type={}",
                req.getMatchId(), req.getTeamId(), req.getPlayerOutId(), req.getPlayerInId(), req.getSubstitutionType());

        String playerOutName = playerRepo.findById(req.getPlayerOutId()).map(Player::getName).orElse(null);
        String playerInName = playerRepo.findById(req.getPlayerInId()).map(Player::getName).orElse(null);

        return new RecordSubstitutionResponse(
                sub.getId(),
                sub.getMatchId(),
                sub.getTeamId(),
                sub.getPlayerOutId(),
                playerOutName,
                sub.getPlayerInId(),
                playerInName,
                sub.getInningsNumber(),
                sub.getOverNumber(),
                sub.getSubstitutionType()
        );
    }

    public DeclareSquadResponse getSquad(GetSquadRequest req) {
        sessionService.validateSession(req.getSessionToken());

        List<MatchSquad> squad = matchSquadRepo.findByMatchIdAndTeamId(req.getMatchId(), req.getTeamId());

        List<String> playerIds = squad.stream().map(MatchSquad::getPlayerId).collect(Collectors.toList());

        Map<String, String> playerNames = playerRepo.findAllById(playerIds).stream()
                .collect(Collectors.toMap(Player::getId, Player::getName));

        List<SquadPlayerEntry> entries = squad.stream()
                .map(s -> new SquadPlayerEntry(s.getPlayerId(), playerNames.get(s.getPlayerId()), s.getRole(), s.isCaptain(), s.isViceCaptain()))
                .collect(Collectors.toList());

        return new DeclareSquadResponse(req.getMatchId(), req.getTeamId(), entries);
    }

    public List<SquadPlayerEntry> getCurrentXI(GetSquadRequest req) {
        sessionService.validateSession(req.getSessionToken());

        List<MatchSquad> squad = matchSquadRepo.findByMatchIdAndTeamId(req.getMatchId(), req.getTeamId());

        Map<String, MatchSquad> squadMap = squad.stream()
                .collect(Collectors.toMap(MatchSquad::getPlayerId, s -> s));

        Set<String> currentXI = squad.stream()
                .filter(s -> s.getRole() == PlayerRole.PLAYING)
                .map(MatchSquad::getPlayerId)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        List<MatchSubstitution> subs = matchSubstitutionRepo
                .findByMatchIdAndTeamIdOrderByInningsNumberAscOverNumberAsc(req.getMatchId(), req.getTeamId());

        for (MatchSubstitution sub : subs) {
            currentXI.remove(sub.getPlayerOutId());
            currentXI.add(sub.getPlayerInId());
        }

        Map<String, String> playerNames = playerRepo.findAllById(currentXI).stream()
                .collect(Collectors.toMap(Player::getId, Player::getName));

        return currentXI.stream()
                .map(pid -> {
                    MatchSquad entry = squadMap.get(pid);
                    boolean isCaptain = entry != null && entry.isCaptain();
                    boolean isViceCaptain = entry != null && entry.isViceCaptain();
                    return new SquadPlayerEntry(pid, playerNames.get(pid), PlayerRole.PLAYING, isCaptain, isViceCaptain);
                })
                .collect(Collectors.toList());
    }
}
