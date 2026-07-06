package com.inklusport.ai.controller;

import com.inklusport.ai.dto.request.FatigueRequest;
import com.inklusport.ai.dto.response.FatigueResponse;
import com.inklusport.ai.service.FatigueDetectionService;
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
@RequestMapping("/api/ai/fatigue")
@RequiredArgsConstructor
@Tag(name = "Fatiga", description = "Detección de fatiga")
public class FatigueController {

    private final FatigueDetectionService fatigueDetectionService;

    @Operation(summary = "Evaluar nivel de fatiga")
    @PostMapping("/evaluate")
    public ResponseEntity<FatigueResponse> evaluate(@Valid @RequestBody FatigueRequest request) {
        return ResponseEntity.ok(fatigueDetectionService.evaluate(request));
    }
}
