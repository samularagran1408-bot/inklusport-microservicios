package com.inklusport.ai.controller;

import com.inklusport.ai.dto.BiomechanicalRequest;
import com.inklusport.ai.dto.BiomechanicalResponse;
import com.inklusport.ai.dto.ErrorResponse;
import com.inklusport.ai.service.BiomechanicalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/ai/biomechanical")
@RequiredArgsConstructor
public class BiomechanicalController {
    
    private final BiomechanicalService biomechanicalService;

    @PostMapping("/analyze")
    public ResponseEntity<?> analyze(@AuthenticationPrincipal String userId,
                                     @Valid @RequestBody BiomechanicalRequest request) {
        try {
            BiomechanicalResponse response = biomechanicalService.analyzeMovement(userId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/ai/biomechanical/analyze");
        }
    }

       private ResponseEntity<ErrorResponse> buildErrorResponse(Exception e, String path) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message(e.getMessage())
                .path(path)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
