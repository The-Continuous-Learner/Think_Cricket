package com.gmh.cricket_app.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gmh.cricket_app.dto.ball.RecordBallRequest;
import com.gmh.cricket_app.dto.ball.RecordBallResponse;
import com.gmh.cricket_app.service.BallService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/balls")
@RequiredArgsConstructor
public class BallController {

    private final BallService ballService;

    @PostMapping("/record")
    public RecordBallResponse recordBall(@RequestBody RecordBallRequest req) {
        return ballService.recordBall(req);
    }
}
