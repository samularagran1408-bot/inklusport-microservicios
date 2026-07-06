package com.inklusport.ai.controller;

import com.inklusport.ai.dto.request.VoiceCommandRequest;
import com.inklusport.ai.dto.response.VoiceResponse;
import com.inklusport.ai.service.VoiceAssistantService;
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
@RequestMapping("/api/ai/voice")
@RequiredArgsConstructor
@Tag(name = "Voz", description = "Asistente por voz")
public class VoiceAssistantController {

    private final VoiceAssistantService voiceAssistantService;

    @Operation(summary = "Procesar comando por voz")
    @PostMapping("/command")
    public ResponseEntity<VoiceResponse> command(@Valid @RequestBody VoiceCommandRequest request) {
        return ResponseEntity.ok(voiceAssistantService.processCommand(request));
    }
}
