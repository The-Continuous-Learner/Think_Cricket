package com.gmh.cricket_app.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gmh.cricket_app.dto.over.EndOverRequest;
import com.gmh.cricket_app.dto.over.EndOverResponse;
import com.gmh.cricket_app.dto.over.StartOverRequest;
import com.gmh.cricket_app.dto.over.StartOverResponse;
import com.gmh.cricket_app.service.OverService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/overs")
@RequiredArgsConstructor
public class OverController {

    private final OverService overService;

    @PostMapping("/start")
    public StartOverResponse startOver(@RequestBody StartOverRequest req) {
        return overService.startOver(req);
    }

    @PostMapping("/end")
    public EndOverResponse endOver(@RequestBody EndOverRequest req) {
        return overService.endOver(req);
    }
}
