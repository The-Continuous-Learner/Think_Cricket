package com.gmh.cricket_app.rest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gmh.cricket_app.dto.squad.DeclareSquadRequest;
import com.gmh.cricket_app.dto.squad.DeclareSquadResponse;
import com.gmh.cricket_app.dto.squad.GetSquadRequest;
import com.gmh.cricket_app.dto.squad.RecordSubstitutionRequest;
import com.gmh.cricket_app.dto.squad.RecordSubstitutionResponse;
import com.gmh.cricket_app.dto.squad.SquadPlayerEntry;
import com.gmh.cricket_app.service.MatchSquadService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/squad")
@RequiredArgsConstructor
public class SquadController {

    private final MatchSquadService matchSquadService;

    @PostMapping("/declare")
    public DeclareSquadResponse declareSquad(@RequestBody DeclareSquadRequest req) {
        return matchSquadService.declareSquad(req);
    }

    @PostMapping("/substitute")
    public RecordSubstitutionResponse recordSubstitution(@RequestBody RecordSubstitutionRequest req) {
        return matchSquadService.recordSubstitution(req);
    }

    @GetMapping("/get")
    public DeclareSquadResponse getSquad(@RequestBody GetSquadRequest req) {
        return matchSquadService.getSquad(req);
    }

    @GetMapping("/current-xi")
    public List<SquadPlayerEntry> getCurrentXI(@RequestBody GetSquadRequest req) {
        return matchSquadService.getCurrentXI(req);
    }
}
