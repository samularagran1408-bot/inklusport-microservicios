package com.inklusport.reports.controller;

import com.inklusport.reports.dto.AnalyticsEventRequest;
import com.inklusport.reports.dto.AnalyticsEventResponse;
import com.inklusport.reports.service.AnalyticsEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    /**
     * Inyección de Servicio
     */
    private final AnalyticsEventService analyticsEventService;

    /**
     * Obtener los reportes de registros a eventos
     * @param userId
     * @param request
     * @return
     */
    @PostMapping("/events")
    public ResponseEntity<AnalyticsEventResponse> registerEvent(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody AnalyticsEventRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(analyticsEventService.registerEvent(request, userId));
    }

    /**
     * Obtener los reportes de registros a eventos por usuario
     * @param userId
     * @return
     */
    @GetMapping("/events/user")
    public ResponseEntity<List<AnalyticsEventResponse>> getUserEvents(@AuthenticationPrincipal String userId) {
        return ResponseEntity.ok(analyticsEventService.getEventsByUser(userId));
    }

    /**
     * Obtener los reportes de registros a eventos por tipo
     * @param eventType
     * @return
     */
    @GetMapping("/events/type/{eventType}")
    public ResponseEntity<List<AnalyticsEventResponse>> getEventsByType(@PathVariable String eventType) {
        return ResponseEntity.ok(analyticsEventService.getEventsByType(eventType));
    }
}