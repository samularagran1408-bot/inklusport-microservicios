package com.inklusport.ai.controller;

import com.inklusport.ai.dto.TrainingPlanRequest;
import com.inklusport.ai.dto.TrainingPlanResponse;
import com.inklusport.ai.dto.ErrorResponse;
import com.inklusport.ai.service.TrainingPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/ai/training")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingPlanService trainingPlanService;

    @PostMapping("/generate")
    public ResponseEntity<?> generatePlan(@AuthenticationPrincipal String userId,
                                           @Valid @RequestBody TrainingPlanRequest request) {
        try {
            TrainingPlanResponse response = trainingPlanService.generatePlan(userId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/ai/training/generate");
        }
    }

    @GetMapping("/plans")
    public ResponseEntity<?> getMyPlans(@AuthenticationPrincipal String userId) {
        try {
            List<TrainingPlanResponse> response = trainingPlanService.getMyPlans(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/ai/training/plans");
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