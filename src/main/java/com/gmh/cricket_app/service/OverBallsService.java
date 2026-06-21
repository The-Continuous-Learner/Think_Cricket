package com.gmh.cricket_app.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.gmh.cricket_app.dto.over.BallInfo;
import com.gmh.cricket_app.dto.over.OverBallsResponse;
import com.gmh.cricket_app.exceptions.BadRequestException;
import com.gmh.cricket_app.models.Ball;
import com.gmh.cricket_app.models.Over;
import com.gmh.cricket_app.models.Player;
import com.gmh.cricket_app.repositories.BallRepository;
import com.gmh.cricket_app.repositories.OverRepository;
import com.gmh.cricket_app.repositories.PlayerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OverBallsService {

    private final OverRepository overRepo;
    private final BallRepository ballRepo;
    private final SessionService sessionService;
    private final PlayerRepository playerRepo;

    public OverBallsResponse getOverBalls(String sessionToken, String overId) {
        sessionService.validateSession(sessionToken);

        Over over = overRepo.findById(overId)
                .orElseThrow(() -> new BadRequestException("Over not found"));

        List<Ball> rawBalls = ballRepo.findByOverIdOrderByBallNumberAsc(overId);

        Set<String> playerIds = rawBalls.stream()
                .flatMap(b -> Stream.of(b.getBatsmanId(), b.getNonStrikerId(), b.getBowlerId()))
                .collect(Collectors.toSet());
        playerIds.add(over.getBowlerId());

        Map<String, String> playerNames = playerRepo.findAllById(playerIds).stream()
                .collect(Collectors.toMap(Player::getId, Player::getName));

        List<BallInfo> balls = rawBalls.stream()
                .map(b -> new BallInfo(
                        b.getId(),
                        b.getBallNumber(),
                        b.isLegalDelivery(),
                        b.getRuns(),
                        b.getExtraRuns(),
                        b.getExtraType(),
                        b.getBoundaryType(),
                        b.getBatsmanId(),
                        playerNames.get(b.getBatsmanId()),
                        b.getNonStrikerId(),
                        playerNames.get(b.getNonStrikerId()),
                        b.getBowlerId(),
                        playerNames.get(b.getBowlerId()),
                        b.isWicket()
                ))
                .collect(Collectors.toList());

        return new OverBallsResponse(
                over.getId(),
                over.getOverNumber(),
                over.getInningsId(),
                over.getBowlerId(),
                playerNames.get(over.getBowlerId()),
                over.getStatus(),
                over.getLegalBallCount(),
                over.getTotalRuns(),
                over.getWickets(),
                balls
        );
    }
}
