package com.gmh.cricket_app.service;

import java.util.List;
import java.util.Map;

import com.gmh.cricket_app.dto.match.DeleteMatchRequest;
import com.gmh.cricket_app.dto.match.EndMatchRequest;
import com.gmh.cricket_app.dto.match.EndMatchResponse;
import com.gmh.cricket_app.dto.match.HostMatchRequest;
import com.gmh.cricket_app.dto.match.HostMatchResponse;
import com.gmh.cricket_app.dto.match.MatchDetailsResponse;
import com.gmh.cricket_app.dto.match.StartMatchRequest;
import com.gmh.cricket_app.dto.match.StartMatchResponse;
import com.gmh.cricket_app.enums.MatchStatus;
import com.gmh.cricket_app.exceptions.BadRequestException;
import com.gmh.cricket_app.models.Match;
import com.gmh.cricket_app.models.User.User;
import com.gmh.cricket_app.models.team.Team;
import com.gmh.cricket_app.repositories.BallRepository;
import com.gmh.cricket_app.repositories.BattingScoreRepository;
import com.gmh.cricket_app.repositories.BowlingScoreRepository;
import com.gmh.cricket_app.repositories.FallOfWicketRepository;
import com.gmh.cricket_app.repositories.MatchRepository;
import com.gmh.cricket_app.repositories.MatchSummaryRepository;
import com.gmh.cricket_app.repositories.TeamRepository;
import com.gmh.cricket_app.repositories.WicketRepository;
import com.gmh.cricket_app.util.CommonUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepo;
    private final SessionService sessionService;
    private final TeamRepository teamRepo;
    private final WicketRepository wicketRepo;
    private final BattingScoreRepository battingScoreRepo;
    private final BowlingScoreRepository bowlingScoreRepo;
    private final FallOfWicketRepository fallOfWicketRepo;
    private final BallRepository ballRepo;
    private final MatchSummaryRepository matchSummaryRepo;

    @Value("${cricket.match.id-length}")
    private int matchIdLength;

    public HostMatchResponse hostMatch(HostMatchRequest req) {

        User user = sessionService.validateSession(req.getSessionToken());

        if (req.getTotalOvers() == null) {
            throw new BadRequestException("totalOvers is required (use 0 for unlimited/Test matches)");
        }

        if (!teamRepo.existsById(req.getTeamAId())) {
            throw new BadRequestException("Team A does not exist: " + req.getTeamAId());
        }
        if (!teamRepo.existsById(req.getTeamBId())) {
            throw new BadRequestException("Team B does not exist: " + req.getTeamBId());
        }
        if (req.getTeamAId().equals(req.getTeamBId())) {
            throw new BadRequestException("Team A and Team B cannot be the same");
        }

        if (req.getParentMatchId() != null && !matchRepo.existsById(req.getParentMatchId())) {
            throw new BadRequestException("Parent match not found: " + req.getParentMatchId());
        }

        Match match = new Match();
        match.setId(CommonUtil.generateId(matchIdLength));
        match.setTeamAId(req.getTeamAId());
        match.setTeamBId(req.getTeamBId());
        match.setFormat(req.getFormat());
        match.setTotalOvers(req.getTotalOvers());
        match.setStatus(MatchStatus.NOT_STARTED);
        match.setPlannedStartTime(req.getPlannedStartTime());
        match.setHostedByUserId(user.getId());
        match.setParentMatchId(req.getParentMatchId());

        matchRepo.save(match);

        log.info("Match hosted: matchId={}, format={}, teamA={}, teamB={}, hostedBy={}, totalOvers={}",
                match.getId(), match.getFormat(), match.getTeamAId(), match.getTeamBId(), user.getId(), match.getTotalOvers());

        return new HostMatchResponse(match.getId(), match.getStatus());
    }

    public StartMatchResponse startMatch(StartMatchRequest req) {

        User user = sessionService.validateSession(req.getSessionToken());

        Match match = matchRepo.findById(req.getMatchId())
                .orElseThrow(() -> new BadRequestException("Match not found"));

        if (match.getStatus() != MatchStatus.NOT_STARTED) {
            log.warn("Start match failed - invalid state: matchId={}, status={}", match.getId(), match.getStatus());
            throw new BadRequestException("Match cannot be started in current state: " + match.getStatus());
        }

        if (matchRepo.existsInProgressMatchInvolvingAnyOf(List.of(match.getTeamAId(), match.getTeamBId())) > 0) {
            log.warn("Start match failed - team already in active match: matchId={}, teamA={}, teamB={}",
                    match.getId(), match.getTeamAId(), match.getTeamBId());
            throw new BadRequestException("One or both teams already have an in-progress match");
        }

        long now = System.currentTimeMillis();
        match.setActualStartTime(now);
        match.setStatus(MatchStatus.IN_PROGRESS);
        match.setStartedByUserId(user.getId());

        matchRepo.save(match);

        log.info("Match started: matchId={}, startedBy={}", match.getId(), user.getId());

        return new StartMatchResponse(
                match.getId(),
                match.getStatus(),
                match.getActualStartTime(),
                match.getStartedByUserId()
        );
    }

    public EndMatchResponse endMatch(EndMatchRequest req) {

        User user = sessionService.validateSession(req.getSessionToken());

        Match match = matchRepo.findById(req.getMatchId())
                .orElseThrow(() -> new BadRequestException("Match not found"));

        MatchStatus finalStatus = req.getFinalStatus();

        if (finalStatus == MatchStatus.COMPLETED || finalStatus == MatchStatus.ABANDONED) {
            if (match.getStatus() != MatchStatus.IN_PROGRESS) {
                log.warn("End match failed - match not IN_PROGRESS: matchId={}, status={}", match.getId(), match.getStatus());
                throw new BadRequestException("Match must be IN_PROGRESS to end as " + finalStatus);
            }
        }
        if (finalStatus == MatchStatus.CANCELLED) {
            if (match.getStatus() != MatchStatus.NOT_STARTED) {
                log.warn("Cancel match failed - match already started: matchId={}, status={}", match.getId(), match.getStatus());
                throw new BadRequestException("Only NOT_STARTED matches can be cancelled");
            }
        }

        long now = System.currentTimeMillis();
        match.setActualEndTime(now);
        match.setStatus(finalStatus);
        match.setEndedByUserId(user.getId());

        matchRepo.save(match);

        log.info("Match ended: matchId={}, finalStatus={}, endedBy={}", match.getId(), finalStatus, user.getId());

        return new EndMatchResponse(
                match.getId(),
                match.getStatus(),
                match.getActualEndTime(),
                match.getEndedByUserId()
        );
    }

    @Transactional
    public void deleteMatch(DeleteMatchRequest req) {
        sessionService.validateSession(req.getSessionToken());

        Match match = matchRepo.findById(req.getMatchId())
                .orElseThrow(() -> new BadRequestException("Match not found"));

        if (match.getStatus() == MatchStatus.IN_PROGRESS) {
            throw new BadRequestException("Cannot delete a match that is IN_PROGRESS");
        }

        wicketRepo.deleteByMatchId(match.getId());
        battingScoreRepo.deleteByMatchId(match.getId());
        bowlingScoreRepo.deleteByMatchId(match.getId());
        fallOfWicketRepo.deleteByMatchId(match.getId());
        matchSummaryRepo.deleteByMatchId(match.getId());
        ballRepo.deleteByMatchId(match.getId());

        matchRepo.delete(match);

        log.info("Match deleted: matchId={}", match.getId());
    }

    public MatchDetailsResponse getMatchDetails(String sessionToken, String matchId) {

        sessionService.validateSession(sessionToken);

        Match match = matchRepo.findById(matchId)
                .orElseThrow(() -> new BadRequestException("Match not found"));

        Map<String, String> teamNames = teamRepo.findAllById(List.of(match.getTeamAId(), match.getTeamBId()))
                .stream().collect(java.util.stream.Collectors.toMap(Team::getId, Team::getName));

        return new MatchDetailsResponse(
                match.getId(),
                match.getTeamAId(), teamNames.get(match.getTeamAId()),
                match.getTeamBId(), teamNames.get(match.getTeamBId()),
                match.getFormat(),
                match.getTotalOvers(),
                match.getStatus(),
                match.getPlannedStartTime(),
                match.getActualStartTime(),
                match.getActualEndTime(),
                match.getHostedByUserId(),
                match.getStartedByUserId(),
                match.getEndedByUserId()
        );
    }
}
