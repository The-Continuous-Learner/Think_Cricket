package com.gmh.cricket_app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gmh.cricket_app.dto.ball.RecordBallRequest;
import com.gmh.cricket_app.dto.ball.RecordBallResponse;
import com.gmh.cricket_app.enums.BoundaryType;
import com.gmh.cricket_app.enums.ExtraType;
import com.gmh.cricket_app.enums.InningsStatus;
import com.gmh.cricket_app.enums.MatchStatus;
import com.gmh.cricket_app.enums.OverStatus;
import com.gmh.cricket_app.exceptions.BadRequestException;
import com.gmh.cricket_app.models.Ball;
import com.gmh.cricket_app.models.BattingScore;
import com.gmh.cricket_app.models.BowlingScore;
import com.gmh.cricket_app.models.Innings;
import com.gmh.cricket_app.models.Match;
import com.gmh.cricket_app.models.Over;
import com.gmh.cricket_app.repositories.BallRepository;
import com.gmh.cricket_app.repositories.BattingScoreRepository;
import com.gmh.cricket_app.repositories.BowlingScoreRepository;
import com.gmh.cricket_app.repositories.InningsRepository;
import com.gmh.cricket_app.repositories.MatchRepository;
import com.gmh.cricket_app.repositories.OverRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BallService {

    private final BallRepository ballRepo;
    private final OverRepository overRepo;
    private final InningsRepository inningsRepo;
    private final MatchRepository matchRepo;
    private final BattingScoreRepository battingScoreRepo;
    private final BowlingScoreRepository bowlingScoreRepo;
    private final SessionService sessionService;

    @Transactional
    public RecordBallResponse recordBall(RecordBallRequest req) {

        sessionService.validateSession(req.getSessionToken());

        Over over = overRepo.findById(req.getOverId())
                .orElseThrow(() -> new BadRequestException("Over not found"));

        if (over.getStatus() != OverStatus.ACTIVE) {
            throw new BadRequestException("Over is not active");
        }

        Innings innings = inningsRepo.findById(over.getInningsId())
                .orElseThrow(() -> new BadRequestException("Innings not found"));

        if (innings.getStatus() != InningsStatus.ACTIVE) {
            throw new BadRequestException("Innings is not active");
        }

        Match match = matchRepo.findById(innings.getMatchId())
                .orElseThrow(() -> new BadRequestException("Match not found"));

        if (match.getStatus() != MatchStatus.IN_PROGRESS) {
            throw new BadRequestException("Match is not in progress");
        }

        String effectiveBowlerId = req.getBowlerOverride() != null
                ? req.getBowlerOverride()
                : over.getBowlerId();

        int ballNumber = (int) ballRepo.countByOverId(req.getOverId()) + 1;
        boolean legalDelivery = req.getExtraType() != ExtraType.WIDE
                && req.getExtraType() != ExtraType.NO_BALL;
        int totalRuns = req.getRuns() + req.getExtraRuns();
        int bowlerRuns = calculateBowlerRuns(req);
        int ballsFacedByBatsman = req.getExtraType() != ExtraType.WIDE ? 1 : 0;

        String ballId = req.getOverId() + "-" + ballNumber;

        // --- Create Ball ---
        Ball ball = new Ball();
        ball.setId(ballId);
        ball.setMatchId(match.getId());
        ball.setInningsId(innings.getId());
        ball.setOverId(over.getId());
        ball.setInningsNumber(innings.getInningsNumber());
        ball.setOverNumber(over.getOverNumber());
        ball.setBallNumber(ballNumber);
        ball.setLegalDelivery(legalDelivery);
        ball.setRuns(req.getRuns());
        ball.setExtraRuns(req.getExtraRuns());
        ball.setExtraType(req.getExtraType());
        ball.setBoundaryType(req.getBoundaryType());
        ball.setBowlerId(effectiveBowlerId);
        ball.setBatsmanId(req.getBatsmanId());
        ball.setNonStrikerId(req.getNonStrikerId());
        ball.setWicket(req.isWicket());
        ballRepo.save(ball);

        // --- Update Over ---
        over.setTotalRuns(over.getTotalRuns() + totalRuns);
        over.setWickets(over.getWickets() + (req.isWicket() ? 1 : 0));
        if (legalDelivery) {
            over.setLegalBallCount(over.getLegalBallCount() + 1);
        }
        overRepo.save(over);

        // --- Update Innings ---
        innings.setTotalRuns(innings.getTotalRuns() + totalRuns);
        innings.setExtras(innings.getExtras() + req.getExtraRuns());
        if (req.isWicket()) {
            innings.setWickets(innings.getWickets() + 1);
        }

        // --- Upsert BattingScore ---
        upsertBattingScore(innings, req, ballsFacedByBatsman);

        // --- Upsert BowlingScore ---
        upsertBowlingScore(innings, effectiveBowlerId, req, legalDelivery, bowlerRuns);

        inningsRepo.save(innings);

        // --- Auto-complete over on 6th legal ball ---
        boolean overCompleted = false;
        if (over.getLegalBallCount() >= 6) {
            over.setStatus(OverStatus.COMPLETED);
            overRepo.save(over);
            overCompleted = true;

            innings.setOversCompleted(innings.getOversCompleted() + 1);

            // Maiden check
            List<Ball> overBalls = ballRepo.findByOverIdOrderByBallNumberAsc(over.getId());
            boolean isMaiden = overBalls.stream()
                    .mapToInt(this::getBowlerRunsFromBall)
                    .sum() == 0;
            if (isMaiden) {
                bowlingScoreRepo.findByInningsIdAndBowlerId(innings.getId(), effectiveBowlerId)
                        .ifPresent(bs -> {
                            bs.setMaidens(bs.getMaidens() + 1);
                            bowlingScoreRepo.save(bs);
                        });
            }

            inningsRepo.save(innings);
        }

        // --- Auto-end innings ---
        boolean inningsCompleted = false;
        if (innings.getWickets() >= 10) {
            innings.setStatus(InningsStatus.COMPLETED);
            inningsRepo.save(innings);
            inningsCompleted = true;
            log.info("Innings auto-ended (10 wickets): inningsId={}", innings.getId());
        } else if (match.getTotalOvers() > 0 && innings.getOversCompleted() >= match.getTotalOvers()) {
            innings.setStatus(InningsStatus.COMPLETED);
            inningsRepo.save(innings);
            inningsCompleted = true;
            log.info("Innings auto-ended (over limit): inningsId={}", innings.getId());
        }

        log.info("Ball recorded: ballId={}, runs={}, extras={}, extraType={}, wicket={}, legal={}",
                ballId, req.getRuns(), req.getExtraRuns(), req.getExtraType(), req.isWicket(), legalDelivery);

        return new RecordBallResponse(
                ballId,
                req.getOverId(),
                innings.getId(),
                ballNumber,
                legalDelivery,
                req.getRuns(),
                req.getExtraRuns(),
                req.getExtraType(),
                req.getBoundaryType(),
                req.isWicket(),
                over.getLegalBallCount(),
                overCompleted,
                inningsCompleted
        );
    }

    private void upsertBattingScore(Innings innings, RecordBallRequest req, int ballsFaced) {

        BattingScore bs = battingScoreRepo
                .findByInningsIdAndPlayerId(innings.getId(), req.getBatsmanId())
                .orElseGet(() -> {
                    BattingScore newBs = new BattingScore();
                    newBs.setId(innings.getId() + "-BAT-" + req.getBatsmanId());
                    newBs.setMatchId(innings.getMatchId());
                    newBs.setInningsId(innings.getId());
                    newBs.setInningsNumber(innings.getInningsNumber());
                    newBs.setPlayerId(req.getBatsmanId());
                    newBs.setBattingPosition((int) battingScoreRepo.countByInningsId(innings.getId()) + 1);
                    return newBs;
                });

        // Runs credited to batsman only for normal and no-ball deliveries (not byes/leg-byes/wides)
        if (req.getExtraType() == null || req.getExtraType() == ExtraType.NO_BALL) {
            bs.setRuns(bs.getRuns() + req.getRuns());
        }
        bs.setBalls(bs.getBalls() + ballsFaced);

        if (req.getBoundaryType() == BoundaryType.FOUR) bs.setFours(bs.getFours() + 1);
        if (req.getBoundaryType() == BoundaryType.SIX) bs.setSixes(bs.getSixes() + 1);

        if (bs.getBalls() > 0) {
            bs.setStrikeRate((double) bs.getRuns() / bs.getBalls() * 100);
        }

        battingScoreRepo.save(bs);
    }

    private void upsertBowlingScore(Innings innings, String bowlerId, RecordBallRequest req,
                                    boolean legalDelivery, int bowlerRuns) {

        BowlingScore bs = bowlingScoreRepo
                .findByInningsIdAndBowlerId(innings.getId(), bowlerId)
                .orElseGet(() -> {
                    BowlingScore newBs = new BowlingScore();
                    newBs.setId(innings.getId() + "-BOWL-" + bowlerId);
                    newBs.setMatchId(innings.getMatchId());
                    newBs.setInningsId(innings.getId());
                    newBs.setInningsNumber(innings.getInningsNumber());
                    newBs.setBowlerId(bowlerId);
                    return newBs;
                });

        bs.setRunsConceded(bs.getRunsConceded() + bowlerRuns);

        if (legalDelivery) {
            bs.setBallsBowled(bs.getBallsBowled() + 1);
            bs.setOvers(bs.getBallsBowled() / 6);
        }

        if (bs.getBallsBowled() > 0) {
            bs.setEconomy(bs.getRunsConceded() * 6.0 / bs.getBallsBowled());
        }

        bowlingScoreRepo.save(bs);
    }

    private int calculateBowlerRuns(RecordBallRequest req) {
        if (req.getExtraType() == null) return req.getRuns();
        return switch (req.getExtraType()) {
            case WIDE -> req.getExtraRuns();
            case NO_BALL -> req.getRuns() + 1;
            case BYE, LEG_BYE -> 0;
        };
    }

    int getBowlerRunsFromBall(Ball ball) {
        if (ball.getExtraType() == null) return ball.getRuns();
        return switch (ball.getExtraType()) {
            case WIDE -> ball.getExtraRuns();
            case NO_BALL -> ball.getRuns() + 1;
            case BYE, LEG_BYE -> 0;
        };
    }
}
