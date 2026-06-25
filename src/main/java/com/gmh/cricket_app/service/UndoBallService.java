package com.gmh.cricket_app.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.gmh.cricket_app.cache.InningsListCache;
import com.gmh.cricket_app.cache.ScorecardCache;
import com.gmh.cricket_app.dto.ball.UndoBallRequest;
import com.gmh.cricket_app.dto.ball.UndoBallResponse;
import com.gmh.cricket_app.enums.BoundaryType;
import com.gmh.cricket_app.enums.ExtraType;
import com.gmh.cricket_app.enums.InningsStatus;
import com.gmh.cricket_app.enums.MatchStatus;
import com.gmh.cricket_app.enums.OverStatus;
import com.gmh.cricket_app.enums.WicketType;
import com.gmh.cricket_app.exceptions.BadRequestException;
import com.gmh.cricket_app.models.Ball;
import com.gmh.cricket_app.models.Innings;
import com.gmh.cricket_app.models.Match;
import com.gmh.cricket_app.models.Over;
import com.gmh.cricket_app.models.Wicket;
import com.gmh.cricket_app.repositories.BallRepository;
import com.gmh.cricket_app.repositories.BattingScoreRepository;
import com.gmh.cricket_app.repositories.BowlingScoreRepository;
import com.gmh.cricket_app.repositories.CurrentBattingStateRepository;
import com.gmh.cricket_app.repositories.FallOfWicketRepository;
import com.gmh.cricket_app.repositories.InningsRepository;
import com.gmh.cricket_app.repositories.MatchRepository;
import com.gmh.cricket_app.repositories.OverRepository;
import com.gmh.cricket_app.repositories.WicketRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UndoBallService {

    private static final Set<WicketType> BOWLER_CREDITED = Set.of(
            WicketType.BOWLED, WicketType.CAUGHT, WicketType.LBW,
            WicketType.STUMPED, WicketType.HIT_WICKET);

    private final BallRepository ballRepo;
    private final OverRepository overRepo;
    private final InningsRepository inningsRepo;
    private final MatchRepository matchRepo;
    private final BattingScoreRepository battingScoreRepo;
    private final BowlingScoreRepository bowlingScoreRepo;
    private final WicketRepository wicketRepo;
    private final FallOfWicketRepository fallOfWicketRepo;
    private final CurrentBattingStateRepository currentBattingStateRepo;
    private final InningsListCache inningsListCache;
    private final ScorecardCache scorecardCache;
    private final SessionService sessionService;

    @Transactional
    public UndoBallResponse undoBall(UndoBallRequest req) {
        sessionService.validateSession(req.getSessionToken());

        Innings innings = inningsRepo.findById(req.getInningsId())
                .orElseThrow(() -> new BadRequestException("Innings not found"));

        Match match = matchRepo.findById(innings.getMatchId())
                .orElseThrow(() -> new BadRequestException("Match not found"));

        if (match.getStatus() != MatchStatus.IN_PROGRESS) {
            throw new BadRequestException("Match is not in progress");
        }

        if (innings.getStatus() != InningsStatus.ACTIVE && innings.getStatus() != InningsStatus.COMPLETED) {
            throw new BadRequestException("Innings does not allow undo");
        }

        Ball ball = ballRepo.findTopByInningsIdOrderByOverNumberDescBallNumberDesc(req.getInningsId())
                .orElseThrow(() -> new BadRequestException("No balls recorded in this innings"));

        Over over = overRepo.findById(ball.getOverId())
                .orElseThrow(() -> new BadRequestException("Over not found"));

        boolean overWasCompleted = over.getStatus() == OverStatus.COMPLETED;
        boolean inningsWasCompleted = innings.getStatus() == InningsStatus.COMPLETED;

        // --- Undo wicket details if recordWicket was called ---
        boolean wicketReversed = false;
        if (ball.isWicket()) {
            wicketReversed = undoWicketDetails(innings, ball);
        }

        // --- Derived values (same logic as recordBall) ---
        int totalRuns = ball.getRuns() + ball.getExtraRuns();
        int bowlerRuns = bowlerRunsFromBall(ball);
        int ballsFaced = ball.getExtraType() != ExtraType.WIDE ? 1 : 0;

        // --- Undo innings aggregate ---
        innings.setTotalRuns(innings.getTotalRuns() - totalRuns);
        innings.setExtras(innings.getExtras() - ball.getExtraRuns());
        if (ball.isWicket()) {
            innings.setWickets(innings.getWickets() - 1);
        }
        if (inningsWasCompleted) {
            innings.setStatus(InningsStatus.ACTIVE);
        }

        // --- Undo over aggregate ---
        over.setTotalRuns(over.getTotalRuns() - totalRuns);
        if (ball.isWicket()) {
            over.setWickets(over.getWickets() - 1);
        }
        if (ball.isLegalDelivery()) {
            over.setLegalBallCount(over.getLegalBallCount() - 1);
        }
        if (overWasCompleted) {
            over.setStatus(OverStatus.ACTIVE);
            innings.setOversCompleted(innings.getOversCompleted() - 1);
            reverseMaidenIfApplicable(innings, over, ball);
        }

        overRepo.save(over);
        inningsRepo.save(innings);

        // --- Undo BattingScore (runs/balls) ---
        battingScoreRepo.findByInningsIdAndPlayerId(innings.getId(), ball.getBatsmanId())
                .ifPresent(bs -> {
                    if (ball.getExtraType() == null || ball.getExtraType() == ExtraType.NO_BALL) {
                        bs.setRuns(bs.getRuns() - ball.getRuns());
                    }
                    bs.setBalls(bs.getBalls() - ballsFaced);
                    if (ball.getBoundaryType() == BoundaryType.FOUR) bs.setFours(bs.getFours() - 1);
                    if (ball.getBoundaryType() == BoundaryType.SIX) bs.setSixes(bs.getSixes() - 1);
                    bs.setStrikeRate(bs.getBalls() > 0 ? (double) bs.getRuns() / bs.getBalls() * 100 : 0.0);
                    battingScoreRepo.save(bs);
                });

        // --- Undo BowlingScore ---
        bowlingScoreRepo.findByInningsIdAndBowlerId(innings.getId(), ball.getBowlerId())
                .ifPresent(bs -> {
                    bs.setRunsConceded(bs.getRunsConceded() - bowlerRuns);
                    if (ball.isLegalDelivery()) {
                        bs.setBallsBowled(bs.getBallsBowled() - 1);
                        bs.setOvers(bs.getBallsBowled() / 6);
                    }
                    bs.setEconomy(bs.getBallsBowled() > 0 ? bs.getRunsConceded() * 6.0 / bs.getBallsBowled() : 0.0);
                    bowlingScoreRepo.save(bs);
                });

        // --- Restore current batting state ---
        long ballsInInnings = ballRepo.countByInningsId(innings.getId());
        if (ballsInInnings <= 1) {
            currentBattingStateRepo.deleteById(innings.getId());
        } else {
            currentBattingStateRepo.findById(innings.getId()).ifPresent(state -> {
                state.setStrikerId(ball.getBatsmanId());
                state.setNonStrikerId(ball.getNonStrikerId());
                currentBattingStateRepo.save(state);
            });
        }

        // --- Evict caches ---
        inningsListCache.evict(innings.getMatchId());
        if (inningsWasCompleted) {
            scorecardCache.evict(innings.getMatchId());
        }

        // --- Delete the ball ---
        ballRepo.delete(ball);

        log.info("Ball undone: ballId={}, inningsId={}, overReopened={}, inningsReopened={}, wicketReversed={}",
                ball.getId(), innings.getId(), overWasCompleted, inningsWasCompleted, wicketReversed);

        return new UndoBallResponse(ball.getId(), innings.getId(), overWasCompleted, inningsWasCompleted, wicketReversed);
    }

    private boolean undoWicketDetails(Innings innings, Ball ball) {
        return wicketRepo.findByBallId(ball.getId()).map(wicket -> {
            battingScoreRepo.findByInningsIdAndPlayerId(innings.getId(), wicket.getPlayerOutId())
                    .ifPresent(bs -> {
                        bs.setOut(false);
                        bs.setDismissalType(null);
                        bs.setDismissalBallId(null);
                        battingScoreRepo.save(bs);
                    });

            if (BOWLER_CREDITED.contains(wicket.getType()) && wicket.getBowlerId() != null) {
                bowlingScoreRepo.findByInningsIdAndBowlerId(innings.getId(), wicket.getBowlerId())
                        .ifPresent(bs -> {
                            bs.setWickets(bs.getWickets() - 1);
                            bowlingScoreRepo.save(bs);
                        });
            }

            fallOfWicketRepo.findByBallId(ball.getId()).ifPresent(fallOfWicketRepo::delete);
            wicketRepo.delete(wicket);
            return true;
        }).orElse(false);
    }

    private void reverseMaidenIfApplicable(Innings innings, Over over, Ball ball) {
        List<Ball> overBalls = ballRepo.findByOverIdOrderByBallNumberAsc(over.getId());
        boolean wasMaiden = overBalls.stream().mapToInt(this::bowlerRunsFromBall).sum() == 0;
        if (wasMaiden) {
            bowlingScoreRepo.findByInningsIdAndBowlerId(innings.getId(), ball.getBowlerId())
                    .ifPresent(bs -> {
                        bs.setMaidens(bs.getMaidens() - 1);
                        bowlingScoreRepo.save(bs);
                    });
        }
    }

    private int bowlerRunsFromBall(Ball ball) {
        if (ball.getExtraType() == null) return ball.getRuns();
        return switch (ball.getExtraType()) {
            case WIDE -> ball.getExtraRuns();
            case NO_BALL -> ball.getRuns() + 1;
            case BYE, LEG_BYE -> 0;
        };
    }
}
