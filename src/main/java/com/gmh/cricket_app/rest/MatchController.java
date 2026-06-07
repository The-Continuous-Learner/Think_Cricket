package com.gmh.cricket_app.rest;

import com.gmh.cricket_app.dto.match.EndMatchRequest;
import com.gmh.cricket_app.dto.match.EndMatchResponse;
import com.gmh.cricket_app.dto.match.HostMatchRequest;
import com.gmh.cricket_app.dto.match.HostMatchResponse;
import com.gmh.cricket_app.dto.match.MatchDetailsRequest;
import com.gmh.cricket_app.dto.match.MatchDetailsResponse;
import com.gmh.cricket_app.dto.match.StartMatchRequest;
import com.gmh.cricket_app.dto.match.StartMatchResponse;
import com.gmh.cricket_app.dto.score.MatchScoreRequest;
import com.gmh.cricket_app.dto.score.MatchScoreResponse;
import com.gmh.cricket_app.service.MatchScoreService;
import com.gmh.cricket_app.service.MatchService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;
    private final MatchScoreService matchScoreService;

    @PostMapping("/host")
    public HostMatchResponse hostMatch(@RequestBody HostMatchRequest request) {
        return matchService.hostMatch(request);
    }

    @PostMapping("/start")
    public StartMatchResponse startMatch(@RequestBody StartMatchRequest request) {
        return matchService.startMatch(request);
    }

    @PostMapping("/end")
    public EndMatchResponse endMatch(@RequestBody EndMatchRequest request) {
        return matchService.endMatch(request);
    }

    @GetMapping("/getDetails")
    public MatchDetailsResponse getMatchDetails(@RequestBody MatchDetailsRequest request) {
        return matchService.getMatchDetails(request.getSessionToken(), request.getMatchId());
    }

    @GetMapping("/score")
    public MatchScoreResponse getScore(@RequestBody MatchScoreRequest request) {
        return matchScoreService.getScore(request.getSessionToken(), request.getMatchId());
    }
}

