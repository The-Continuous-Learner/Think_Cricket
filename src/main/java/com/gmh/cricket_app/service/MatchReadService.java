package com.gmh.cricket_app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gmh.cricket_app.dto.match.ActiveInningsSummary;
import com.gmh.cricket_app.dto.match.ActiveOverSummary;
import com.gmh.cricket_app.dto.match.LiveStateResponse;
import com.gmh.cricket_app.dto.match.MatchSummary;
import com.gmh.cricket_app.enums.InningsStatus;
import com.gmh.cricket_app.enums.OverStatus;
import com.gmh.cricket_app.exceptions.BadRequestException;
import com.gmh.cricket_app.models.Ball;
import com.gmh.cricket_app.models.Innings;
import com.gmh.cricket_app.models.Match;
import com.gmh.cricket_app.models.Over;
import com.gmh.cricket_app.models.User.User;
import com.gmh.cricket_app.repositories.BallRepository;
import com.gmh.cricket_app.repositories.InningsRepository;
import com.gmh.cricket_app.repositories.MatchRepository;
import com.gmh.cricket_app.repositories.OverRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatchReadService {

    private final MatchRepository matchRepo;
    private final InningsRepository inningsRepo;
    private final OverRepository overRepo;
    private final BallRepository ballRepo;
    private final SessionService sessionService;

    public List<MatchSummary> getMatchList(String sessionToken) {
        User user = sessionService.validateSession(sessionToken);
        return matchRepo.findByHostedByUserId(user.getId())
                .stream()
                .map(m -> new MatchSummary(
                        m.getId(), m.getFormat(), m.getStatus(), m.getTotalOvers(),
                        m.getTeamAId(), m.getTeamBId(),
                        m.getPlannedStartTime(), m.getActualStartTime()))
                .collect(Collectors.toList());
    }

    public LiveStateResponse getLiveState(String sessionToken, String matchId) {
        sessionService.validateSession(sessionToken);

        Match match = matchRepo.findById(matchId)
                .orElseThrow(() -> new BadRequestException("Match not found"));

        List<Innings> inningsList = inningsRepo.findByMatchIdOrderByInningsNumberAsc(matchId);

        Innings activeInnings = inningsList.stream()
                .filter(i -> i.getStatus() == InningsStatus.ACTIVE)
                .findFirst()
                .orElse(null);

        if (activeInnings == null) {
            return new LiveStateResponse(
                    match.getId(), match.getFormat(), match.getStatus(), match.getTotalOvers(),
                    match.getTeamAId(), match.getTeamBId(), null, null, null, null);
        }

        Integer target = null;
        if (match.getTotalOvers() > 0 && activeInnings.getInningsNumber() == 2) {
            target = inningsList.stream()
                    .filter(i -> i.getInningsNumber() == 1)
                    .findFirst()
                    .map(i -> i.getTotalRuns() + 1)
                    .orElse(null);
        }

        ActiveInningsSummary inningsSummary = new ActiveInningsSummary(
                activeInnings.getId(),
                activeInnings.getInningsNumber(),
                activeInnings.getBattingTeamId(),
                activeInnings.getBowlingTeamId(),
                activeInnings.getStatus(),
                activeInnings.getTotalRuns(),
                activeInnings.getWickets(),
                activeInnings.getOversCompleted(),
                activeInnings.getExtras(),
                target
        );

        Over activeOver = overRepo
                .findByInningsIdAndStatus(activeInnings.getId(), OverStatus.ACTIVE)
                .orElse(null);

        ActiveOverSummary overSummary = activeOver == null ? null : new ActiveOverSummary(
                activeOver.getId(),
                activeOver.getOverNumber(),
                activeOver.getBowlerId(),
                activeOver.getStatus(),
                activeOver.getLegalBallCount(),
                activeOver.getTotalRuns(),
                activeOver.getWickets()
        );

        Ball lastBall = ballRepo
                .findTopByInningsIdOrderByOverNumberDescBallNumberDesc(activeInnings.getId())
                .orElse(null);

        return new LiveStateResponse(
                match.getId(), match.getFormat(), match.getStatus(), match.getTotalOvers(),
                match.getTeamAId(), match.getTeamBId(),
                inningsSummary, overSummary,
                lastBall != null ? lastBall.getBatsmanId() : null,
                lastBall != null ? lastBall.getNonStrikerId() : null
        );
    }
}
