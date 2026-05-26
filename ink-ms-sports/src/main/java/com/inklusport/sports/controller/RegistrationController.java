package com.inklusport.sports.controller;

import com.inklusport.sports.dto.request.RegistrationRequest;
import com.inklusport.sports.dto.response.RegistrationResponse;
import com.inklusport.sports.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/registrations")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> registerToEvent(@RequestBody RegistrationRequest request) { // 🌟 Cambiado a <?> para soportar respuestas de error mixtas
        try {
            RegistrationResponse response = registrationService.registerToEvent(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "ERROR",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "FATAL_ERROR",
                    "message", "Error inesperado: " + e.getMessage()
            ));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> cancelRegistration(@PathVariable String id) {
        try {
            registrationService.cancelRegistration(id);
            return ResponseEntity.ok(Map.of(
                    "status", "SUCCESS",
                    "message", "Inscripción cancelada correctamente y lista de espera actualizada."
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "ERROR",
                    "message", e.getMessage()
            ));
        }
    }

    @GetMapping("da503a79-ed37-4b7c-9231-c4eb65ff9531{eventId}/waitlist")
    public ResponseEntity<List<RegistrationResponse>> getWaitlist(@PathVariable String eventId) {
        return ResponseEntity.ok(registrationService.getWaitlistForEvent(eventId));
    }
}