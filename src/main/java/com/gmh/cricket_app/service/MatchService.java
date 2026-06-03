package com.gmh.cricket_app.service;

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
import com.gmh.cricket_app.repositories.MatchRepository;
import com.gmh.cricket_app.repositories.TeamRepository;
import com.gmh.cricket_app.util.CommonUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepo;
    private final SessionService sessionService;
    private final TeamRepository teamRepo;

    @Value("${cricket.match.id-length}")
    private int matchIdLength;

    public HostMatchResponse hostMatch(HostMatchRequest req) {

        User user = sessionService.validateSession(req.getSessionToken());

        // Validate team A
        if (!teamRepo.existsById(req.getTeamAId())) {
            throw new BadRequestException("Team A does not exist: " + req.getTeamAId());
        }

        // Validate team B
        if (!teamRepo.existsById(req.getTeamBId())) {
            throw new BadRequestException("Team B does not exist: " + req.getTeamBId());
        }

        // Prevent same team on both sides
        if (req.getTeamAId().equals(req.getTeamBId())) {
            throw new BadRequestException("Team A and Team B cannot be the same");
        }

        // Create match
        Match match = new Match();
        match.setId(CommonUtil.generateId(matchIdLength));
        match.setTeamAId(req.getTeamAId());
        match.setTeamBId(req.getTeamBId());
        match.setFormat(req.getFormat());
        match.setStatus(MatchStatus.NOT_STARTED);
        match.setPlannedStartTime(req.getPlannedStartTime());
        match.setHostedByUserId(user.getId());

        matchRepo.save(match);

        return new HostMatchResponse(match.getId(), match.getStatus());
    }


    public StartMatchResponse startMatch(StartMatchRequest req) {

        // Validate session
        User user = sessionService.validateSession(req.getSessionToken());

        // Fetch match
        Match match = matchRepo.findById(req.getMatchId())
                .orElseThrow(() -> new BadRequestException("Match not found"));

        // Validate state
        if (match.getStatus() != MatchStatus.NOT_STARTED) {
            throw new BadRequestException("Match cannot be started in current state: " + match.getStatus());
        }

        // Set start details
        long now = System.currentTimeMillis();
        match.setActualStartTime(now);
        match.setStatus(MatchStatus.IN_PROGRESS);
        match.setStartedByUserId(user.getId());

        matchRepo.save(match);

        return new StartMatchResponse(
                match.getId(),
                match.getStatus(),
                match.getActualStartTime(),
                match.getStartedByUserId()
        );
    }

    public EndMatchResponse endMatch(EndMatchRequest req) {

        // Validate session
        User user = sessionService.validateSession(req.getSessionToken());

        // Fetch match
        Match match = matchRepo.findById(req.getMatchId())
                .orElseThrow(() -> new BadRequestException("Match not found"));

        MatchStatus finalStatus = req.getFinalStatus();

        // Validate transitions
        if (finalStatus == MatchStatus.COMPLETED || finalStatus == MatchStatus.ABANDONED) {
            if (match.getStatus() != MatchStatus.IN_PROGRESS) {
                throw new BadRequestException("Match must be IN_PROGRESS to end as " + finalStatus);
            }
        }
        if (finalStatus == MatchStatus.CANCELLED) {
            if (match.getStatus() != MatchStatus.NOT_STARTED) {
                throw new BadRequestException("Only NOT_STARTED matches can be cancelled");
            }
        }

        // Set end details
        long now = System.currentTimeMillis();
        match.setActualEndTime(now);
        match.setStatus(finalStatus);
        match.setEndedByUserId(user.getId());

        matchRepo.save(match);

        return new EndMatchResponse(
                match.getId(),
                match.getStatus(),
                match.getActualEndTime(),
                match.getEndedByUserId()
        );
    }

    public MatchDetailsResponse getMatchDetails(String sessionToken, String matchId) {

        // Validate session
        sessionService.validateSession(sessionToken);

        // Fetch match
        Match match = matchRepo.findById(matchId)
                .orElseThrow(() -> new BadRequestException("Match not found"));

        // Build response DTO
        return new MatchDetailsResponse(
                match.getId(),
                match.getTeamAId(),
                match.getTeamBId(),
                match.getFormat(),
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
