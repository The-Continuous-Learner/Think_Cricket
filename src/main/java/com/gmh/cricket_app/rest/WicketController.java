package com.gmh.cricket_app.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gmh.cricket_app.dto.wicket.RecordWicketRequest;
import com.gmh.cricket_app.dto.wicket.RecordWicketResponse;
import com.gmh.cricket_app.service.WicketService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/wickets")
@RequiredArgsConstructor
public class WicketController {

    private final WicketService wicketService;

    @PostMapping("/record")
    public RecordWicketResponse recordWicket(@RequestBody RecordWicketRequest req) {
        return wicketService.recordWicket(req);
    }
}
