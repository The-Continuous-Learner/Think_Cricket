package com.gmh.cricket_app.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gmh.cricket_app.dto.toss.ConductTossRequest;
import com.gmh.cricket_app.dto.toss.ConductTossResponse;
import com.gmh.cricket_app.dto.toss.FlipCoinRequest;
import com.gmh.cricket_app.dto.toss.GetTossDetailsRequest;
import com.gmh.cricket_app.enums.TossResult;
import com.gmh.cricket_app.service.TossService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/toss")
@RequiredArgsConstructor
public class TossController {

    private final TossService tossService;

    @PostMapping("/conduct")
    public ConductTossResponse conductToss(@RequestBody ConductTossRequest req) {
        return tossService.conductToss(req);
    }

    @GetMapping
    public ConductTossResponse getToss(@RequestBody GetTossDetailsRequest req) {
        return tossService.getToss(req.getSessionToken(), req.getMatchId());
    }

    @PostMapping("/flip")
    public TossResult flipCoin(@RequestBody FlipCoinRequest req) {
        return tossService.flipCoin(req.getSessionToken());
    }
}
