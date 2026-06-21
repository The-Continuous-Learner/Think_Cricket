package com.gmh.cricket_app.service;

import java.util.Random;

import org.springframework.stereotype.Service;

import com.gmh.cricket_app.dto.toss.ConductTossRequest;
import com.gmh.cricket_app.dto.toss.ConductTossResponse;
import com.gmh.cricket_app.enums.MatchStatus;
import com.gmh.cricket_app.enums.TossResult;
import com.gmh.cricket_app.exceptions.BadRequestException;
import com.gmh.cricket_app.models.Match;
import com.gmh.cricket_app.models.Toss;
import com.gmh.cricket_app.models.team.Team;
import com.gmh.cricket_app.repositories.MatchRepository;
import com.gmh.cricket_app.repositories.TeamRepository;
import com.gmh.cricket_app.repositories.TossRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TossService {

    private final TossRepository tossRepo;
    private final MatchRepository matchRepo;
    private final SessionService sessionService;
    private final TeamRepository teamRepo;

    public ConductTossResponse conductToss(ConductTossRequest req) {

        sessionService.validateSession(req.getSessionToken());

        Match match = matchRepo.findById(req.getMatchId())
                .orElseThrow(() -> new BadRequestException("Match not found"));

        if (match.getStatus() != MatchStatus.IN_PROGRESS) {
            log.warn("Toss failed - match not IN_PROGRESS: matchId={}, status={}", match.getId(), match.getStatus());
            throw new BadRequestException("Toss can only be conducted for an IN_PROGRESS match");
        }

        if (tossRepo.existsByMatchId(req.getMatchId())) {
            log.warn("Toss failed - already conducted: matchId={}", req.getMatchId());
            throw new BadRequestException("Toss already conducted for this match");
        }

        String winnerTeamId = req.getWinnerTeamId();
        if (!winnerTeamId.equals(match.getTeamAId()) && !winnerTeamId.equals(match.getTeamBId())) {
            log.warn("Toss failed - winner team not in match: matchId={}, winnerTeamId={}", match.getId(), winnerTeamId);
            throw new BadRequestException("Winner team does not belong to this match");
        }

        String loserTeamId = winnerTeamId.equals(match.getTeamAId()) ? match.getTeamBId() : match.getTeamAId();

        Toss toss = new Toss(
                req.getMatchId() + "-toss",
                req.getMatchId(),
                req.getTossResult(),
                winnerTeamId,
                req.getDecision()
        );

        tossRepo.save(toss);

        log.info("Toss conducted: matchId={}, result={}, winner={}, decision={}",
                toss.getMatchId(), toss.getTossResult(), toss.getWinnerTeamId(), toss.getDecision());

        String winnerTeamName = teamRepo.findById(winnerTeamId).map(Team::getName).orElse(null);
        String loserTeamName = teamRepo.findById(loserTeamId).map(Team::getName).orElse(null);

        return new ConductTossResponse(
                toss.getId(),
                toss.getMatchId(),
                toss.getTossResult(),
                toss.getWinnerTeamId(),
                winnerTeamName,
                loserTeamId,
                loserTeamName,
                toss.getDecision()
        );
    }

    public ConductTossResponse getToss(String sessionToken, String matchId) {

        sessionService.validateSession(sessionToken);

        Match match = matchRepo.findById(matchId)
                .orElseThrow(() -> new BadRequestException("Match not found"));

        Toss toss = tossRepo.findByMatchId(matchId)
                .orElseThrow(() -> new BadRequestException("Toss not yet conducted for this match"));

        String loserTeamId = toss.getWinnerTeamId().equals(match.getTeamAId())
                ? match.getTeamBId()
                : match.getTeamAId();

        String winnerTeamName = teamRepo.findById(toss.getWinnerTeamId()).map(Team::getName).orElse(null);
        String loserTeamName = teamRepo.findById(loserTeamId).map(Team::getName).orElse(null);

        return new ConductTossResponse(
                toss.getId(),
                toss.getMatchId(),
                toss.getTossResult(),
                toss.getWinnerTeamId(),
                winnerTeamName,
                loserTeamId,
                loserTeamName,
                toss.getDecision()
        );
    }

    public TossResult flipCoin(String sessionToken) {
        sessionService.validateSession(sessionToken);
        TossResult[] values = TossResult.values();
        return values[new Random().nextInt(values.length)];
    }
}
