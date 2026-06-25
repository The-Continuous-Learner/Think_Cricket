package com.gmh.cricket_app.rest;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gmh.cricket_app.dto.ball.RecordBallRequest;
import com.gmh.cricket_app.dto.ball.RecordBallResponse;
import com.gmh.cricket_app.dto.ball.UndoBallRequest;
import com.gmh.cricket_app.dto.ball.UndoBallResponse;
import com.gmh.cricket_app.service.BallService;
import com.gmh.cricket_app.service.UndoBallService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/balls")
@RequiredArgsConstructor
public class BallController {

    private final BallService ballService;
    private final UndoBallService undoBallService;

    @PostMapping("/record")
    public RecordBallResponse recordBall(@RequestBody @Validated RecordBallRequest req) {
        return ballService.recordBall(req);
    }

    @PostMapping("/undo")
    public UndoBallResponse undoBall(@RequestBody UndoBallRequest req) {
        return undoBallService.undoBall(req);
    }
}
