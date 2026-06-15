package com.inklusport.ai.controller;

import com.inklusport.ai.dto.FeedbackRequest;
import com.inklusport.ai.dto.FeedbackResponse;
import com.inklusport.ai.dto.FeedbackStats;
import com.inklusport.ai.dto.ErrorResponse;
import com.inklusport.ai.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/ai/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<?> registrarFeedback(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody FeedbackRequest request) {
        try {
            FeedbackResponse response = feedbackService.registrarFeedback(userId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/ai/feedback");
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getMyFeedbacks(@AuthenticationPrincipal String userId) {
        try {
            List<FeedbackResponse> response = feedbackService.getFeedbacksByUser(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/ai/feedback/user");
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getFeedbackStats() {
        try {
            FeedbackStats response = feedbackService.getFeedbackStats();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/ai/feedback/stats");
        }
    }

    @GetMapping("/conversacion/{conversacionId}")
    public ResponseEntity<?> getFeedbacksByConversacion(@PathVariable String conversacionId) {
        try {
            List<FeedbackResponse> response = feedbackService.getFeedbacksByConversacion(conversacionId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/ai/feedback/conversacion/" + conversacionId);
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