package com.gmh.cricket_app.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import com.gmh.cricket_app.models.Player;
import com.gmh.cricket_app.models.team.Team;
import com.gmh.cricket_app.repositories.BattingScoreRepository;
import com.gmh.cricket_app.repositories.BowlingScoreRepository;
import com.gmh.cricket_app.repositories.FallOfWicketRepository;
import com.gmh.cricket_app.repositories.InningsRepository;
import com.gmh.cricket_app.repositories.MatchRepository;
import com.gmh.cricket_app.repositories.PlayerRepository;
import com.gmh.cricket_app.repositories.TeamRepository;

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
    private final TeamRepository teamRepo;
    private final PlayerRepository playerRepo;

    public MatchScoreResponse getScore(String sessionToken, String matchId) {

        sessionService.validateSession(sessionToken);

        Match match = matchRepo.findById(matchId)
                .orElseThrow(() -> new BadRequestException("Match not found"));

        Map<String, String> teamNames = teamRepo.findAllById(Set.of(match.getTeamAId(), match.getTeamBId()))
                .stream().collect(Collectors.toMap(Team::getId, Team::getName));

        List<Innings> inningsList = inningsRepo.findByMatchIdOrderByInningsNumberAsc(matchId);

        Set<String> playerIds = new HashSet<>();
        for (Innings innings : inningsList) {
            battingScoreRepo.findByInningsIdOrderByBattingPositionAsc(innings.getId())
                    .forEach(bs -> playerIds.add(bs.getPlayerId()));
            bowlingScoreRepo.findByInningsId(innings.getId())
                    .forEach(bs -> playerIds.add(bs.getBowlerId()));
            fallOfWicketRepo.findByInningsIdOrderByWicketNumberAsc(innings.getId())
                    .forEach(fow -> {
                        playerIds.add(fow.getPlayerOutId());
                        if (fow.getBowlerId() != null) playerIds.add(fow.getBowlerId());
                        if (fow.getFielderId() != null) playerIds.add(fow.getFielderId());
                    });
        }
        Map<String, String> playerNames = playerIds.isEmpty() ? Map.of() :
                playerRepo.findAllById(playerIds).stream()
                        .collect(Collectors.toMap(Player::getId, Player::getName));

        List<InningsSummary> inningsSummaries = inningsList.stream()
                .map(innings -> buildInningsSummary(innings, teamNames, playerNames))
                .collect(Collectors.toList());

        return new MatchScoreResponse(
                match.getId(),
                match.getFormat(),
                match.getStatus(),
                match.getTotalOvers(),
                match.getTeamAId(), teamNames.get(match.getTeamAId()),
                match.getTeamBId(), teamNames.get(match.getTeamBId()),
                inningsSummaries
        );
    }

    private InningsSummary buildInningsSummary(Innings innings, Map<String, String> teamNames, Map<String, String> playerNames) {

        List<BattingLine> batting = battingScoreRepo
                .findByInningsIdOrderByBattingPositionAsc(innings.getId())
                .stream()
                .map(bs -> new BattingLine(
                        bs.getPlayerId(),
                        playerNames.get(bs.getPlayerId()),
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
                        playerNames.get(bs.getBowlerId()),
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
                        playerNames.get(fow.getPlayerOutId()),
                        fow.getBowlerId(),
                        playerNames.get(fow.getBowlerId()),
                        fow.getFielderId(),
                        playerNames.get(fow.getFielderId())
                ))
                .collect(Collectors.toList());

        return new InningsSummary(
                innings.getInningsNumber(),
                innings.getBattingTeamId(),
                teamNames.get(innings.getBattingTeamId()),
                innings.getBowlingTeamId(),
                teamNames.get(innings.getBowlingTeamId()),
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
