package com.gmh.cricket_app.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gmh.cricket_app.dto.innings.EndInningsRequest;
import com.gmh.cricket_app.dto.innings.EndInningsResponse;
import com.gmh.cricket_app.dto.innings.StartInningsRequest;
import com.gmh.cricket_app.dto.innings.StartInningsResponse;
import com.gmh.cricket_app.service.InningsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/innings")
@RequiredArgsConstructor
public class InningsController {

    private final InningsService inningsService;

    @PostMapping("/start")
    public StartInningsResponse startInnings(@RequestBody StartInningsRequest req) {
        return inningsService.startInnings(req);
    }

    @PostMapping("/end")
    public EndInningsResponse endInnings(@RequestBody EndInningsRequest req) {
        return inningsService.endInnings(req);
    }
}
