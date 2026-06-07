package com.gmh.cricket_app.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.gmh.cricket_app.dto.wicket.RecordWicketRequest;
import com.gmh.cricket_app.dto.wicket.RecordWicketResponse;
import com.gmh.cricket_app.enums.WicketType;
import com.gmh.cricket_app.exceptions.BadRequestException;
import com.gmh.cricket_app.models.Ball;
import com.gmh.cricket_app.models.FallOfWicket;
import com.gmh.cricket_app.models.Innings;
import com.gmh.cricket_app.models.Wicket;
import com.gmh.cricket_app.repositories.BallRepository;
import com.gmh.cricket_app.repositories.BattingScoreRepository;
import com.gmh.cricket_app.repositories.BowlingScoreRepository;
import com.gmh.cricket_app.repositories.FallOfWicketRepository;
import com.gmh.cricket_app.repositories.InningsRepository;
import com.gmh.cricket_app.repositories.TeamPlayerMapperRepository;
import com.gmh.cricket_app.repositories.WicketRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WicketService {

    private static final Set<WicketType> BOWLER_CREDITED = Set.of(
            WicketType.BOWLED, WicketType.CAUGHT, WicketType.LBW,
            WicketType.STUMPED, WicketType.HIT_WICKET);

    private final BallRepository ballRepo;
    private final WicketRepository wicketRepo;
    private final FallOfWicketRepository fallOfWicketRepo;
    private final InningsRepository inningsRepo;
    private final BattingScoreRepository battingScoreRepo;
    private final BowlingScoreRepository bowlingScoreRepo;
    private final TeamPlayerMapperRepository teamPlayerMapperRepo;
    private final SessionService sessionService;

    @Transactional
    public RecordWicketResponse recordWicket(RecordWicketRequest req) {

        sessionService.validateSession(req.getSessionToken());

        Ball ball = ballRepo.findById(req.getBallId())
                .orElseThrow(() -> new BadRequestException("Ball not found"));

        if (!ball.isWicket()) {
            throw new BadRequestException("Ball is not marked as a wicket");
        }

        if (wicketRepo.findByBallId(req.getBallId()).isPresent()) {
            throw new BadRequestException("Wicket already recorded for this ball");
        }

        Innings innings = inningsRepo.findById(ball.getInningsId())
                .orElseThrow(() -> new BadRequestException("Innings not found"));

        validatePlayerTeamMembership(innings, req);

        int wicketNumber = (int) fallOfWicketRepo.countByInningsId(innings.getId()) + 1;
        int teamScoreAtFall = innings.getTotalRuns();

        // --- Create Wicket ---
        String wicketId = ball.getId() + "-WKT";
        Wicket wicket = new Wicket();
        wicket.setId(wicketId);
        wicket.setMatchId(innings.getMatchId());
        wicket.setInningsId(innings.getId());
        wicket.setOverId(ball.getOverId());
        wicket.setBallId(ball.getId());
        wicket.setPlayerOutId(req.getPlayerOutId());
        wicket.setBowlerId(req.getBowlerId());
        wicket.setFielderId(req.getFielderId());
        wicket.setType(req.getType());
        wicketRepo.save(wicket);

        // --- Create FallOfWicket ---
        FallOfWicket fow = new FallOfWicket();
        fow.setId(innings.getId() + "-FOW-" + wicketNumber);
        fow.setMatchId(innings.getMatchId());
        fow.setInningsId(innings.getId());
        fow.setInningsNumber(innings.getInningsNumber());
        fow.setWicketNumber(wicketNumber);
        fow.setTeamScoreAtFall(teamScoreAtFall);
        fow.setOverNumber(ball.getOverNumber());
        fow.setBallNumber(ball.getBallNumber());
        fow.setPlayerOutId(req.getPlayerOutId());
        fow.setBowlerId(req.getBowlerId());
        fow.setFielderId(req.getFielderId());
        fow.setBallId(ball.getId());
        fallOfWicketRepo.save(fow);

        // --- Update BattingScore: mark batter out ---
        battingScoreRepo.findByInningsIdAndPlayerId(innings.getId(), req.getPlayerOutId())
                .ifPresent(bs -> {
                    bs.setOut(true);
                    bs.setDismissalType(req.getType().name());
                    bs.setDismissalBallId(ball.getId());
                    battingScoreRepo.save(bs);
                });

        // --- Credit bowler wicket (only for bowler-attributed dismissals) ---
        if (BOWLER_CREDITED.contains(req.getType()) && req.getBowlerId() != null) {
            bowlingScoreRepo.findByInningsIdAndBowlerId(innings.getId(), req.getBowlerId())
                    .ifPresent(bs -> {
                        bs.setWickets(bs.getWickets() + 1);
                        bowlingScoreRepo.save(bs);
                    });
        }

        log.info("Wicket recorded: wicketId={}, playerOut={}, type={}, inningsId={}",
                wicketId, req.getPlayerOutId(), req.getType(), innings.getId());

        return new RecordWicketResponse(
                wicketId,
                ball.getId(),
                innings.getId(),
                req.getPlayerOutId(),
                req.getType(),
                wicketNumber,
                teamScoreAtFall
        );
    }

    private void validatePlayerTeamMembership(Innings innings, RecordWicketRequest req) {
        String bat = innings.getBattingTeamId();
        String bowl = innings.getBowlingTeamId();

        if (!teamPlayerMapperRepo.existsByTeamIdAndPlayerId(bat, req.getPlayerOutId()))
            throw new BadRequestException("Player out not found in batting team");
        if (req.getBowlerId() != null
                && !teamPlayerMapperRepo.existsByTeamIdAndPlayerId(bowl, req.getBowlerId()))
            throw new BadRequestException("Bowler not found in bowling team");
        if (req.getFielderId() != null
                && !teamPlayerMapperRepo.existsByTeamIdAndPlayerId(bowl, req.getFielderId()))
            throw new BadRequestException("Fielder not found in bowling team");
    }
}
