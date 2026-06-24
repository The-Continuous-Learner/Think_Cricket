package com.gmh.cricket_app.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gmh.cricket_app.dto.match.CompleteMatchRequest;
import com.gmh.cricket_app.dto.match.CompleteMatchResponse;
import com.gmh.cricket_app.enums.InningsStatus;
import com.gmh.cricket_app.enums.MatchStatus;
import com.gmh.cricket_app.exceptions.BadRequestException;
import com.gmh.cricket_app.models.Innings;
import com.gmh.cricket_app.models.Match;
import com.gmh.cricket_app.models.MatchResult;
import com.gmh.cricket_app.models.team.Team;
import com.gmh.cricket_app.repositories.InningsRepository;
import com.gmh.cricket_app.repositories.MatchRepository;
import com.gmh.cricket_app.repositories.MatchResultRepository;
import com.gmh.cricket_app.repositories.TeamRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchResultService {

    private final MatchRepository matchRepo;
    private final InningsRepository inningsRepo;
    private final MatchResultRepository matchResultRepo;
    private final SessionService sessionService;
    private final TeamRepository teamRepo;

    public CompleteMatchResponse completeMatch(CompleteMatchRequest req) {

        sessionService.validateSession(req.getSessionToken());

        Match match = matchRepo.findById(req.getMatchId())
                .orElseThrow(() -> new BadRequestException("Match not found"));

        if (match.getStatus() != MatchStatus.IN_PROGRESS) {
            throw new BadRequestException("Match is not in progress");
        }

        List<Innings> innings = inningsRepo.findByMatchIdOrderByInningsNumberAsc(req.getMatchId());

        boolean hasActive = innings.stream().anyMatch(i -> i.getStatus() == InningsStatus.ACTIVE);
        if (hasActive) {
            throw new BadRequestException("Cannot complete match while an innings is in progress");
        }

        Map<String, String> teamNames = teamRepo.findAllById(java.util.Set.of(match.getTeamAId(), match.getTeamBId()))
                .stream().collect(Collectors.toMap(Team::getId, Team::getName));

        String winnerTeamId = null;
        String loserTeamId = null;
        boolean isDraw = false;
        String resultText;

        if (innings.size() < 2) {
            isDraw = true;
            resultText = "No result";
        } else {
            Map<String, Integer> runsByTeam = innings.stream()
                    .collect(Collectors.groupingBy(Innings::getBattingTeamId,
                            Collectors.summingInt(Innings::getTotalRuns)));

            int teamARuns = runsByTeam.getOrDefault(match.getTeamAId(), 0);
            int teamBRuns = runsByTeam.getOrDefault(match.getTeamBId(), 0);

            if (teamARuns == teamBRuns) {
                isDraw = true;
                resultText = "Match tied";
            } else {
                winnerTeamId = teamARuns > teamBRuns ? match.getTeamAId() : match.getTeamBId();
                loserTeamId = winnerTeamId.equals(match.getTeamAId()) ? match.getTeamBId() : match.getTeamAId();

                String winnerName = teamNames.getOrDefault(winnerTeamId, winnerTeamId);
                Innings lastInnings = innings.get(innings.size() - 1);
                int runMargin = Math.abs(teamARuns - teamBRuns);

                if (winnerTeamId.equals(lastInnings.getBattingTeamId())) {
                    int wicketsRemaining = 10 - lastInnings.getWickets();
                    resultText = winnerName + " won by " + wicketsRemaining + " wickets";
                } else {
                    final String finalWinnerId = winnerTeamId;
                    long winnerInningsCount = innings.stream()
                            .filter(i -> i.getBattingTeamId().equals(finalWinnerId))
                            .count();

                    if (isTestMatch(match) && winnerInningsCount == 1) {
                        resultText = winnerName + " won by an innings and " + runMargin + " runs";
                    } else {
                        resultText = winnerName + " won by " + runMargin + " runs";
                    }
                }
            }
        }

        boolean decidedBySuperOver = match.getParentMatchId() != null;

        MatchResult result = new MatchResult(
                req.getMatchId() + "-RESULT",
                req.getMatchId(),
                winnerTeamId,
                loserTeamId,
                isDraw,
                resultText,
                decidedBySuperOver
        );

        matchResultRepo.save(result);

        match.setStatus(MatchStatus.COMPLETED);
        match.setActualEndTime(System.currentTimeMillis());
        matchRepo.save(match);

        log.info("Match completed: matchId={}, result={}", match.getId(), resultText);

        String winnerTeamName = winnerTeamId != null ? teamNames.get(winnerTeamId) : null;
        String loserTeamName = loserTeamId != null ? teamNames.get(loserTeamId) : null;

        return new CompleteMatchResponse(
                match.getId(),
                winnerTeamId,
                winnerTeamName,
                loserTeamId,
                loserTeamName,
                isDraw,
                resultText,
                decidedBySuperOver
        );
    }

    public CompleteMatchResponse getResult(String sessionToken, String matchId) {

        sessionService.validateSession(sessionToken);

        matchRepo.findById(matchId)
                .orElseThrow(() -> new BadRequestException("Match not found"));

        MatchResult result = matchResultRepo.findByMatchId(matchId)
                .orElseThrow(() -> new BadRequestException("Result not yet available for this match"));

        String winnerTeamName = result.getWinnerTeamId() != null ? teamRepo.findById(result.getWinnerTeamId()).map(Team::getName).orElse(null) : null;
        String loserTeamName = result.getLoserTeamId() != null ? teamRepo.findById(result.getLoserTeamId()).map(Team::getName).orElse(null) : null;

        return new CompleteMatchResponse(
                result.getMatchId(),
                result.getWinnerTeamId(),
                winnerTeamName,
                result.getLoserTeamId(),
                loserTeamName,
                result.isDraw(),
                result.getResultText(),
                result.isDecidedBySuperOver()
        );
    }

    private boolean isTestMatch(Match match) {
        return match.getTotalOvers() == 0;
    }
}
