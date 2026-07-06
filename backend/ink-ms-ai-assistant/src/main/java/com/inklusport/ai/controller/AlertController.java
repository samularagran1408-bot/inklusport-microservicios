package com.inklusport.ai.controller;

import com.inklusport.ai.dto.request.AlertRequest;
import com.inklusport.ai.dto.response.AlertResponse;
import com.inklusport.ai.service.AlertService;
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
@RequestMapping("/api/ai/alerts")
@RequiredArgsConstructor
@Tag(name = "Alertas", description = "Alertas inteligentes")
public class AlertController {

    private final AlertService alertService;

    @Operation(summary = "Generar alerta inteligente")
    @PostMapping("/trigger")
    public ResponseEntity<AlertResponse> trigger(@Valid @RequestBody AlertRequest request) {
        return ResponseEntity.ok(alertService.trigger(request));
    }
}
