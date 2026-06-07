package com.gmh.cricket_app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gmh.cricket_app.dto.score.BattingLine;
import com.gmh.cricket_app.dto.score.BowlingLine;
import com.gmh.cricket_app.dto.score.FallOfWicketLine;
import com.gmh.cricket_app.dto.score.InningsSummary;
import com.gmh.cricket_app.dto.score.MatchScoreResponse;
import com.gmh.cricket_app.exceptions.BadRequestException;
import com.gmh.cricket_app.models.Innings;
import com.gmh.cricket_app.models.Match;
import com.gmh.cricket_app.repositories.BattingScoreRepository;
import com.gmh.cricket_app.repositories.BowlingScoreRepository;
import com.gmh.cricket_app.repositories.FallOfWicketRepository;
import com.gmh.cricket_app.repositories.InningsRepository;
import com.gmh.cricket_app.repositories.MatchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatchScoreService {

    private final MatchRepository matchRepo;
    private final InningsRepository inningsRepo;
    private final BattingScoreRepository battingScoreRepo;
    private final BowlingScoreRepository bowlingScoreRepo;
    private final FallOfWicketRepository fallOfWicketRepo;
    private final SessionService sessionService;

    public MatchScoreResponse getScore(String sessionToken, String matchId) {

        sessionService.validateSession(sessionToken);

        Match match = matchRepo.findById(matchId)
                .orElseThrow(() -> new BadRequestException("Match not found"));

        List<InningsSummary> inningsSummaries = inningsRepo
                .findByMatchIdOrderByInningsNumberAsc(matchId)
                .stream()
                .map(this::buildInningsSummary)
                .collect(Collectors.toList());

        return new MatchScoreResponse(
                match.getId(),
                match.getFormat(),
                match.getStatus(),
                match.getTotalOvers(),
                match.getTeamAId(),
                match.getTeamBId(),
                inningsSummaries
        );
    }

    private InningsSummary buildInningsSummary(Innings innings) {

        List<BattingLine> batting = battingScoreRepo
                .findByInningsIdOrderByBattingPositionAsc(innings.getId())
                .stream()
                .map(bs -> new BattingLine(
                        bs.getPlayerId(),
                        bs.getBattingPosition(),
                        bs.getRuns(),
                        bs.getBalls(),
                        bs.getFours(),
                        bs.getSixes(),
                        bs.getStrikeRate(),
                        bs.isOut(),
                        bs.getDismissalType(),
                        bs.getDismissalBallId()
                ))
                .collect(Collectors.toList());

        List<BowlingLine> bowling = bowlingScoreRepo
                .findByInningsId(innings.getId())
                .stream()
                .map(bs -> new BowlingLine(
                        bs.getBowlerId(),
                        bs.getOvers(),
                        bs.getBallsBowled(),
                        bs.getMaidens(),
                        bs.getRunsConceded(),
                        bs.getWickets(),
                        bs.getEconomy()
                ))
                .collect(Collectors.toList());

        List<FallOfWicketLine> fallOfWickets = fallOfWicketRepo
                .findByInningsIdOrderByWicketNumberAsc(innings.getId())
                .stream()
                .map(fow -> new FallOfWicketLine(
                        fow.getWicketNumber(),
                        fow.getTeamScoreAtFall(),
                        fow.getOverNumber(),
                        fow.getBallNumber(),
                        fow.getPlayerOutId(),
                        fow.getBowlerId(),
                        fow.getFielderId()
                ))
                .collect(Collectors.toList());

        return new InningsSummary(
                innings.getInningsNumber(),
                innings.getBattingTeamId(),
                innings.getBowlingTeamId(),
                innings.getStatus(),
                innings.getTotalRuns(),
                innings.getWickets(),
                innings.getOversCompleted(),
                innings.getExtras(),
                batting,
                bowling,
                fallOfWickets
        );
    }
}
