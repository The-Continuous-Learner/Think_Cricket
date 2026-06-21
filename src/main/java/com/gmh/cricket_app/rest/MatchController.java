package com.gmh.cricket_app.rest;

import com.gmh.cricket_app.dto.match.EndMatchRequest;
import com.gmh.cricket_app.dto.match.EndMatchResponse;
import com.gmh.cricket_app.dto.match.HostMatchRequest;
import com.gmh.cricket_app.dto.match.HostMatchResponse;
import com.gmh.cricket_app.dto.match.MatchDetailsRequest;
import com.gmh.cricket_app.dto.match.MatchDetailsResponse;
import com.gmh.cricket_app.dto.match.StartMatchRequest;
import com.gmh.cricket_app.dto.match.StartMatchResponse;
import com.gmh.cricket_app.dto.match.LiveStateRequest;
import com.gmh.cricket_app.dto.match.LiveStateResponse;
import com.gmh.cricket_app.dto.match.MatchListRequest;
import com.gmh.cricket_app.dto.match.MatchSummary;
import com.gmh.cricket_app.dto.score.MatchScoreRequest;
import com.gmh.cricket_app.dto.score.MatchScoreResponse;
import com.gmh.cricket_app.service.MatchReadService;
import com.gmh.cricket_app.service.MatchScoreService;
import com.gmh.cricket_app.service.MatchService;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;
    private final MatchScoreService matchScoreService;
    private final MatchReadService matchReadService;

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

    @GetMapping("/list")
    public List<MatchSummary> getMatchList(@RequestBody MatchListRequest request) {
        return matchReadService.getMatchList(request.getSessionToken());
    }

    @GetMapping("/live-state")
    public LiveStateResponse getLiveState(@RequestBody LiveStateRequest request) {
        return matchReadService.getLiveState(request.getSessionToken(), request.getMatchId());
    }

    @GetMapping("/recent")
    public List<MatchSummary> getRecentMatches(@RequestBody MatchListRequest request) {
        return matchReadService.getRecentMatches(request.getSessionToken());
    }
}

