package com.gmh.cricket_app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gmh.cricket_app.dto.innings.EndInningsRequest;
import com.gmh.cricket_app.dto.innings.EndInningsResponse;
import com.gmh.cricket_app.dto.innings.StartInningsRequest;
import com.gmh.cricket_app.dto.innings.StartInningsResponse;
import com.gmh.cricket_app.enums.InningsStatus;
import com.gmh.cricket_app.enums.MatchStatus;
import com.gmh.cricket_app.exceptions.BadRequestException;
import com.gmh.cricket_app.models.Innings;
import com.gmh.cricket_app.models.Match;
import com.gmh.cricket_app.models.team.Team;
import com.gmh.cricket_app.repositories.InningsRepository;
import com.gmh.cricket_app.repositories.MatchRepository;
import com.gmh.cricket_app.repositories.TeamRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class InningsService {

    private final InningsRepository inningsRepo;
    private final MatchRepository matchRepo;
    private final SessionService sessionService;
    private final TeamRepository teamRepo;

    public StartInningsResponse startInnings(StartInningsRequest req) {

        sessionService.validateSession(req.getSessionToken());

        Match match = matchRepo.findById(req.getMatchId())
                .orElseThrow(() -> new BadRequestException("Match not found"));

        if (match.getStatus() != MatchStatus.IN_PROGRESS) {
            throw new BadRequestException("Match is not in progress");
        }

        if (req.getBattingTeamId().equals(req.getBowlingTeamId())) {
            throw new BadRequestException("Batting and bowling teams cannot be the same");
        }

        if (!req.getBattingTeamId().equals(match.getTeamAId()) && !req.getBattingTeamId().equals(match.getTeamBId())) {
            throw new BadRequestException("Batting team does not belong to this match");
        }

        if (!req.getBowlingTeamId().equals(match.getTeamAId()) && !req.getBowlingTeamId().equals(match.getTeamBId())) {
            throw new BadRequestException("Bowling team does not belong to this match");
        }

        int maxInnings = isTestMatch(match) ? 4 : 2;

        List<Innings> existingInnings = inningsRepo.findByMatchIdOrderByInningsNumberAsc(req.getMatchId());

        if (existingInnings.size() >= maxInnings) {
            throw new BadRequestException("All innings have already been played for this match");
        }

        boolean hasActiveInnings = existingInnings.stream()
                .anyMatch(i -> i.getStatus() == InningsStatus.ACTIVE);
        if (hasActiveInnings) {
            throw new BadRequestException("An innings is already in progress");
        }

        int inningsNumber = existingInnings.size() + 1;

        if (inningsNumber > 1) {
            Innings previous = existingInnings.get(inningsNumber - 2);
            if (previous.getStatus() != InningsStatus.COMPLETED) {
                throw new BadRequestException("Innings " + (inningsNumber - 1) + " must be completed before starting innings " + inningsNumber);
            }
        }

        Innings innings = new Innings();
        innings.setId(req.getMatchId() + "-INN-" + inningsNumber);
        innings.setMatchId(req.getMatchId());
        innings.setInningsNumber(inningsNumber);
        innings.setBattingTeamId(req.getBattingTeamId());
        innings.setBowlingTeamId(req.getBowlingTeamId());
        innings.setStatus(InningsStatus.ACTIVE);
        innings.setTotalRuns(0);
        innings.setWickets(0);
        innings.setExtras(0);
        innings.setOversCompleted(0);

        inningsRepo.save(innings);

        log.info("Innings started: inningsId={}, inningsNumber={}, battingTeam={}, bowlingTeam={}",
                innings.getId(), inningsNumber, req.getBattingTeamId(), req.getBowlingTeamId());

        String battingTeamName = teamRepo.findById(req.getBattingTeamId()).map(Team::getName).orElse(null);
        String bowlingTeamName = teamRepo.findById(req.getBowlingTeamId()).map(Team::getName).orElse(null);

        return new StartInningsResponse(
                innings.getId(),
                innings.getMatchId(),
                innings.getInningsNumber(),
                innings.getBattingTeamId(),
                battingTeamName,
                innings.getBowlingTeamId(),
                bowlingTeamName,
                null,
                innings.getStatus()
        );
    }

    public EndInningsResponse endInnings(EndInningsRequest req) {

        sessionService.validateSession(req.getSessionToken());

        Innings innings = inningsRepo.findById(req.getInningsId())
                .orElseThrow(() -> new BadRequestException("Innings not found"));

        if (innings.getStatus() != InningsStatus.ACTIVE) {
            throw new BadRequestException("Innings is not active");
        }

        Match match = matchRepo.findById(innings.getMatchId())
                .orElseThrow(() -> new BadRequestException("Match not found"));

        if (match.getStatus() != MatchStatus.IN_PROGRESS) {
            throw new BadRequestException("Match is not in progress");
        }

        innings.setStatus(InningsStatus.COMPLETED);
        inningsRepo.save(innings);

        log.info("Innings ended: inningsId={}, inningsNumber={}, totalRuns={}, wickets={}, overs={}",
                innings.getId(), innings.getInningsNumber(), innings.getTotalRuns(),
                innings.getWickets(), innings.getOversCompleted());

        return new EndInningsResponse(
                innings.getId(),
                innings.getMatchId(),
                innings.getInningsNumber(),
                innings.getTotalRuns(),
                innings.getWickets(),
                innings.getExtras(),
                innings.getOversCompleted(),
                innings.getStatus()
        );
    }

    private boolean isTestMatch(Match match) {
        return match.getTotalOvers() == 0;
    }
}
