package com.gmh.cricket_app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gmh.cricket_app.cache.InningsListCache;
import com.gmh.cricket_app.cache.ScorecardCache;
import com.gmh.cricket_app.dto.innings.EndInningsRequest;
import com.gmh.cricket_app.dto.innings.EndInningsResponse;
import com.gmh.cricket_app.dto.innings.GetInningsListRequest;
import com.gmh.cricket_app.dto.innings.GetScorecardRequest;
import com.gmh.cricket_app.dto.innings.InningsSummary;
import com.gmh.cricket_app.dto.innings.ScoreCardResponse;
import com.gmh.cricket_app.dto.innings.StartInningsRequest;
import com.gmh.cricket_app.dto.innings.StartInningsResponse;
import com.gmh.cricket_app.dto.innings.InningsScoreCard;
import com.gmh.cricket_app.enums.InningsStatus;
import com.gmh.cricket_app.enums.MatchStatus;
import com.gmh.cricket_app.enums.PlayerRole;
import com.gmh.cricket_app.models.BattingScore;
import com.gmh.cricket_app.exceptions.BadRequestException;
import com.gmh.cricket_app.models.Innings;
import com.gmh.cricket_app.models.Match;
import com.gmh.cricket_app.models.team.Team;
import com.gmh.cricket_app.repositories.BattingScoreRepository;
import com.gmh.cricket_app.repositories.BowlingScoreRepository;
import com.gmh.cricket_app.repositories.FallOfWicketRepository;
import com.gmh.cricket_app.repositories.InningsRepository;
import com.gmh.cricket_app.repositories.MatchRepository;
import com.gmh.cricket_app.repositories.MatchSquadRepository;
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
    private final MatchSquadRepository matchSquadRepo;
    private final BattingScoreRepository battingScoreRepo;
    private final BowlingScoreRepository bowlingScoreRepo;
    private final FallOfWicketRepository fallOfWicketRepo;
    private final ScorecardCache scorecardCache;
    private final InningsListCache inningsListCache;

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

        if (!matchSquadRepo.existsByMatchIdAndTeamId(req.getMatchId(), match.getTeamAId())) {
            throw new BadRequestException("Squad not declared for team A");
        }

        if (!matchSquadRepo.existsByMatchIdAndTeamId(req.getMatchId(), match.getTeamBId())) {
            throw new BadRequestException("Squad not declared for team B");
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
            if (!isTestMatch(match) && previous.getBattingTeamId().equals(req.getBattingTeamId())) {
                throw new BadRequestException("The same team cannot bat in consecutive innings in a limited-overs match");
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

        fillMissingBattingEntries(innings);

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

    public List<InningsSummary> getInningsList(GetInningsListRequest req) {
        sessionService.validateSession(req.getSessionToken());

        Optional<List<InningsSummary>> cached = inningsListCache.get(req.getMatchId());
        if (cached.isPresent()) {
            log.info("InningsList cache hit: matchId={}", req.getMatchId());
            return cached.get();
        }

        Match match = matchRepo.findById(req.getMatchId())
                .orElseThrow(() -> new BadRequestException("Match not found"));

        Map<String, String> teamNames = teamRepo.findAllById(Set.of(match.getTeamAId(), match.getTeamBId()))
                .stream().collect(Collectors.toMap(Team::getId, Team::getName));

        List<Innings> inningsList = inningsRepo.findByMatchIdOrderByInningsNumberAsc(req.getMatchId());

        List<InningsSummary> result = new ArrayList<>();
        for (int i = 0; i < inningsList.size(); i++) {
            Innings inn = inningsList.get(i);
            Integer target = i > 0 ? inningsList.get(i - 1).getTotalRuns() + 1 : null;
            result.add(new InningsSummary(
                    inn.getId(),
                    inn.getInningsNumber(),
                    inn.getBattingTeamId(),
                    teamNames.get(inn.getBattingTeamId()),
                    inn.getBowlingTeamId(),
                    teamNames.get(inn.getBowlingTeamId()),
                    inn.getStatus(),
                    inn.getTotalRuns(),
                    inn.getWickets(),
                    inn.getOversCompleted(),
                    inn.getExtras(),
                    target
            ));
        }

        inningsListCache.put(req.getMatchId(), result);
        return result;
    }

    public ScoreCardResponse getScorecard(GetScorecardRequest req) {
        sessionService.validateSession(req.getSessionToken());

        Optional<ScoreCardResponse> cached = scorecardCache.get(req.getMatchId());
        if (cached.isPresent()) {
            log.info("Scorecard cache hit: matchId={}", req.getMatchId());
            return cached.get();
        }

        Match match = matchRepo.findById(req.getMatchId())
                .orElseThrow(() -> new BadRequestException("Match not found"));

        Map<String, String> teamNames = teamRepo.findAllById(Set.of(match.getTeamAId(), match.getTeamBId()))
                .stream().collect(Collectors.toMap(Team::getId, Team::getName));

        List<Innings> inningsList = inningsRepo.findByMatchIdOrderByInningsNumberAsc(req.getMatchId());

        List<InningsScoreCard> cards = inningsList.stream()
                .map(inn -> new InningsScoreCard(
                        inn.getId(),
                        inn.getInningsNumber(),
                        inn.getBattingTeamId(),
                        inn.getBowlingTeamId(),
                        battingScoreRepo.findByInningsIdOrderByBattingPositionAsc(inn.getId()),
                        bowlingScoreRepo.findByInningsId(inn.getId()),
                        fallOfWicketRepo.findByInningsIdOrderByWicketNumberAsc(inn.getId()),
                        inn.getTotalRuns(),
                        inn.getWickets(),
                        inn.getOversCompleted()
                ))
                .collect(Collectors.toList());

        String resultText = computeResultText(match, inningsList, teamNames);

        ScoreCardResponse response = new ScoreCardResponse(req.getMatchId(), match.getStatus().name(), resultText, cards);

        if (match.getStatus() == MatchStatus.COMPLETED) {
            scorecardCache.put(req.getMatchId(), response);
        }

        return response;
    }

    private String computeResultText(Match match, List<Innings> inningsList, Map<String, String> teamNames) {
        if (match.getStatus() != MatchStatus.COMPLETED || inningsList.size() < 2) {
            return null;
        }
        Innings inn1 = inningsList.get(0);
        Innings inn2 = inningsList.get(1);
        if (inn2.getTotalRuns() > inn1.getTotalRuns()) {
            int wicketsInHand = 10 - inn2.getWickets();
            String winner = teamNames.getOrDefault(inn2.getBattingTeamId(), inn2.getBattingTeamId());
            return winner + " won by " + wicketsInHand + (wicketsInHand == 1 ? " wicket" : " wickets");
        } else if (inn1.getTotalRuns() > inn2.getTotalRuns()) {
            int margin = inn1.getTotalRuns() - inn2.getTotalRuns();
            String winner = teamNames.getOrDefault(inn1.getBattingTeamId(), inn1.getBattingTeamId());
            return winner + " won by " + margin + (margin == 1 ? " run" : " runs");
        } else {
            return "Match tied";
        }
    }

    private void fillMissingBattingEntries(Innings innings) {
        Set<String> alreadyBatted = battingScoreRepo
                .findByInningsIdOrderByBattingPositionAsc(innings.getId())
                .stream()
                .map(BattingScore::getPlayerId)
                .collect(Collectors.toSet());

        List<String> playingXI = matchSquadRepo
                .findByMatchIdAndTeamId(innings.getMatchId(), innings.getBattingTeamId())
                .stream()
                .filter(s -> s.getRole() == PlayerRole.PLAYING)
                .map(s -> s.getPlayerId())
                .collect(Collectors.toList());

        int nextPosition = alreadyBatted.size() + 1;
        for (String playerId : playingXI) {
            if (!alreadyBatted.contains(playerId)) {
                BattingScore bs = new BattingScore();
                bs.setId(innings.getId() + "-BAT-" + playerId);
                bs.setMatchId(innings.getMatchId());
                bs.setInningsId(innings.getId());
                bs.setInningsNumber(innings.getInningsNumber());
                bs.setPlayerId(playerId);
                bs.setBattingPosition(nextPosition++);
                bs.setRuns(0);
                bs.setBalls(0);
                bs.setFours(0);
                bs.setSixes(0);
                bs.setOut(false);
                bs.setStrikeRate(0.0);
                battingScoreRepo.save(bs);
            }
        }
    }

    private boolean isTestMatch(Match match) {
        return match.getTotalOvers() == 0;
    }
}
