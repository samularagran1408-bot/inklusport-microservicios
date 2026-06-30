package com.inklusport.ai.controller;

import com.inklusport.ai.dto.FeedbackRequest;
import com.inklusport.ai.dto.FeedbackResponse;
import com.inklusport.ai.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai/feedback")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Feedback", description = "Endpoints de feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Operation(summary = "Guardar feedback de una respuesta")
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FeedbackResponse> saveFeedback(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody FeedbackRequest request) {

        log.info("💬 Guardando feedback de usuario: {}", userId);

        if (userId != null && !userId.isEmpty()) {
            request.setUsuarioId(userId);
        }

        FeedbackResponse response = feedbackService.saveFeedback(
                userId != null ? userId : request.getUsuarioId(),
                request
        );

        return ResponseEntity.ok(response);
    }
}