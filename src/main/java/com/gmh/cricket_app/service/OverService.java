package com.gmh.cricket_app.service;

import org.springframework.stereotype.Service;

import com.gmh.cricket_app.dto.over.EndOverRequest;
import com.gmh.cricket_app.dto.over.EndOverResponse;
import com.gmh.cricket_app.dto.over.StartOverRequest;
import com.gmh.cricket_app.dto.over.StartOverResponse;
import com.gmh.cricket_app.enums.InningsStatus;
import com.gmh.cricket_app.enums.MatchStatus;
import com.gmh.cricket_app.enums.OverStatus;
import com.gmh.cricket_app.exceptions.BadRequestException;
import com.gmh.cricket_app.models.Innings;
import com.gmh.cricket_app.models.Match;
import com.gmh.cricket_app.models.Over;
import com.gmh.cricket_app.models.Player;
import com.gmh.cricket_app.repositories.InningsRepository;
import com.gmh.cricket_app.repositories.MatchRepository;
import com.gmh.cricket_app.repositories.OverRepository;
import com.gmh.cricket_app.repositories.PlayerRepository;
import com.gmh.cricket_app.repositories.TeamPlayerMapperRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OverService {

    private final OverRepository overRepo;
    private final InningsRepository inningsRepo;
    private final MatchRepository matchRepo;
    private final TeamPlayerMapperRepository teamPlayerMapperRepo;
    private final SessionService sessionService;
    private final PlayerRepository playerRepo;

    public StartOverResponse startOver(StartOverRequest req) {

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

        // Check over limit for non-Test matches
        if (match.getTotalOvers() > 0 && innings.getOversCompleted() >= match.getTotalOvers()) {
            throw new BadRequestException("Over limit reached for this innings");
        }

        // Validate no active over already in this innings
        if (overRepo.findByInningsIdAndStatus(req.getInningsId(), OverStatus.ACTIVE).isPresent()) {
            throw new BadRequestException("An over is already in progress for this innings");
        }

        // Validate bowler belongs to the bowling team
        if (!teamPlayerMapperRepo.existsByTeamIdAndPlayerId(innings.getBowlingTeamId(), req.getBowlerId())) {
            throw new BadRequestException("Bowler does not belong to the bowling team");
        }

        overRepo.findTopByInningsIdAndStatusOrderByOverNumberDesc(req.getInningsId(), OverStatus.COMPLETED)
                .ifPresent(prev -> {
                    if (prev.getBowlerId().equals(req.getBowlerId())) {
                        throw new BadRequestException("Bowler cannot bowl consecutive overs");
                    }
                });

        int overNumber = innings.getOversCompleted() + 1;

        Over over = new Over();
        over.setId(innings.getId() + "-OVR-" + overNumber);
        over.setMatchId(innings.getMatchId());
        over.setInningsId(innings.getId());
        over.setInningsNumber(innings.getInningsNumber());
        over.setOverNumber(overNumber);
        over.setBowlerId(req.getBowlerId());
        over.setStatus(OverStatus.ACTIVE);
        over.setTotalRuns(0);
        over.setWickets(0);

        overRepo.save(over);

        log.info("Over started: overId={}, inningsId={}, overNumber={}, bowler={}",
                over.getId(), innings.getId(), overNumber, req.getBowlerId());

        String bowlerName = playerRepo.findById(over.getBowlerId()).map(Player::getName).orElse(null);

        return new StartOverResponse(
                over.getId(),
                over.getInningsId(),
                over.getMatchId(),
                over.getOverNumber(),
                over.getBowlerId(),
                bowlerName,
                over.getStatus()
        );
    }

    public EndOverResponse endOver(EndOverRequest req) {

        sessionService.validateSession(req.getSessionToken());

        Over over = overRepo.findById(req.getOverId())
                .orElseThrow(() -> new BadRequestException("Over not found"));

        if (over.getStatus() != OverStatus.ACTIVE) {
            throw new BadRequestException("Over is not active");
        }

        Innings innings = inningsRepo.findById(over.getInningsId())
                .orElseThrow(() -> new BadRequestException("Innings not found"));

        over.setStatus(OverStatus.COMPLETED);
        overRepo.save(over);

        innings.setOversCompleted(innings.getOversCompleted() + 1);
        inningsRepo.save(innings);

        log.info("Over ended: overId={}, inningsId={}, overNumber={}, totalRuns={}, wickets={}, inningsOvers={}",
                over.getId(), innings.getId(), over.getOverNumber(),
                over.getTotalRuns(), over.getWickets(), innings.getOversCompleted());

        String bowlerName = playerRepo.findById(over.getBowlerId()).map(Player::getName).orElse(null);

        return new EndOverResponse(
                over.getId(),
                over.getInningsId(),
                over.getOverNumber(),
                over.getBowlerId(),
                bowlerName,
                over.getTotalRuns(),
                over.getWickets(),
                over.getStatus(),
                innings.getOversCompleted()
        );
    }
}
