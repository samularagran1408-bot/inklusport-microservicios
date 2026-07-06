package com.inklusport.ai.controller;

import com.inklusport.ai.dto.request.WearableDataRequest;
import com.inklusport.ai.dto.response.AlertResponse;
import com.inklusport.ai.service.WearableDataService;
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
@RequestMapping("/api/ai/wearables")
@RequiredArgsConstructor
@Tag(name = "Wearables", description = "Integración con wearables")
public class WearableController {

    private final WearableDataService wearableDataService;

    @Operation(summary = "Registrar datos de wearable")
    @PostMapping("/ingest")
    public ResponseEntity<AlertResponse> ingest(@Valid @RequestBody WearableDataRequest request) {
        return ResponseEntity.ok(wearableDataService.ingest(request));
    }
}
