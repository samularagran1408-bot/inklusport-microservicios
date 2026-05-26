package com.inklusport.sports.controller;

import com.inklusport.sports.dto.request.AttendanceRequest;
import com.inklusport.sports.service.EventAttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class EventAttendanceController {

    private final EventAttendanceService eventAttendanceService;

    @PostMapping
    @PreAuthorize("isAuthenticated()") 
    public ResponseEntity<?> markAttendance(@RequestBody AttendanceRequest request) {
        try {
            String successMessage = eventAttendanceService.recordAttendance(request);
            return ResponseEntity.ok(Map.of(
                    "status", "SUCCESS",
                    "message", successMessage
            ));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "ERROR",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "FATAL_ERROR",
                    "message", "Ocurrió un error inesperado al procesar la asistencia."
            ));
        }
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAttendances(
            @RequestParam(required = false) String eventId,
            @RequestParam(required = false) String registrationId) {
        try {
            if (registrationId != null && !registrationId.trim().isEmpty()) {
                return ResponseEntity.ok(eventAttendanceService.getAttendancesByRegistration(registrationId));
            }
            
            if (eventId != null && !eventId.trim().isEmpty()) {
                return ResponseEntity.ok(eventAttendanceService.getAttendancesByEvent(eventId));
            }
            return ResponseEntity.ok(eventAttendanceService.getAllAttendances());
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "ERROR",
                    "message", "Error al recuperar las asistencias: " + e.getMessage()
            ));
        }
    }
}