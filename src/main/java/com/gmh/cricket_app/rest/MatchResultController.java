package com.gmh.cricket_app.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gmh.cricket_app.dto.match.CompleteMatchRequest;
import com.gmh.cricket_app.dto.match.CompleteMatchResponse;
import com.gmh.cricket_app.dto.match.MatchResultRequest;
import com.gmh.cricket_app.service.MatchResultService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/match-result")
@RequiredArgsConstructor
public class MatchResultController {

    private final MatchResultService matchResultService;

    @PostMapping("/complete")
    public CompleteMatchResponse completeMatch(@RequestBody CompleteMatchRequest req) {
        return matchResultService.completeMatch(req);
    }

    @GetMapping
    public CompleteMatchResponse getResult(@RequestBody MatchResultRequest req) {
        return matchResultService.getResult(req.getSessionToken(), req.getMatchId());
    }
}
