package com.inklusport.ai.controller;

import com.inklusport.ai.dto.request.CompetitionRequest;
import com.inklusport.ai.dto.response.CompetitionResponse;
import com.inklusport.ai.service.CompetitionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai/competition")
@RequiredArgsConstructor
@Tag(name = "Competencia", description = "Modo competencia")
public class CompetitionController {

    private final CompetitionService competitionService;

    @Operation(summary = "Preparar modo competencia")
    @PostMapping("/prepare")
    public ResponseEntity<CompetitionResponse> prepare(@Valid @RequestBody CompetitionRequest request) {
        return ResponseEntity.ok(competitionService.prepare(request));
    }
}
