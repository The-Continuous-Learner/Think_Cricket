package com.gmh.cricket_app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gmh.cricket_app.dto.over.BallInfo;
import com.gmh.cricket_app.dto.over.OverBallsResponse;
import com.gmh.cricket_app.exceptions.BadRequestException;
import com.gmh.cricket_app.models.Over;
import com.gmh.cricket_app.repositories.BallRepository;
import com.gmh.cricket_app.repositories.OverRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OverBallsService {

    private final OverRepository overRepo;
    private final BallRepository ballRepo;
    private final SessionService sessionService;

    public OverBallsResponse getOverBalls(String sessionToken, String overId) {
        sessionService.validateSession(sessionToken);

        Over over = overRepo.findById(overId)
                .orElseThrow(() -> new BadRequestException("Over not found"));

        List<BallInfo> balls = ballRepo.findByOverIdOrderByBallNumberAsc(overId)
                .stream()
                .map(b -> new BallInfo(
                        b.getId(),
                        b.getBallNumber(),
                        b.isLegalDelivery(),
                        b.getRuns(),
                        b.getExtraRuns(),
                        b.getExtraType(),
                        b.getBoundaryType(),
                        b.getBatsmanId(),
                        b.getNonStrikerId(),
                        b.getBowlerId(),
                        b.isWicket()
                ))
                .collect(Collectors.toList());

        return new OverBallsResponse(
                over.getId(),
                over.getOverNumber(),
                over.getInningsId(),
                over.getBowlerId(),
                over.getStatus(),
                over.getLegalBallCount(),
                over.getTotalRuns(),
                over.getWickets(),
                balls
        );
    }
}
