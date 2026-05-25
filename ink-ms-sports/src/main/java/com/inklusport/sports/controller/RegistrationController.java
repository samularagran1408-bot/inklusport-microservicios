package com.inklusport.sports.controller;

import com.inklusport.sports.client.UserServiceClient;
import com.inklusport.sports.dto.request.RegistrationRequest;
import com.inklusport.sports.dto.request.WaitlistRequest;
import com.inklusport.sports.dto.response.RegistrationResponse;
import com.inklusport.sports.dto.response.WaitlistResponse;
import com.inklusport.sports.dto.response.ErrorResponse;
import com.inklusport.sports.service.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/registrations")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Registrations", description = "Endpoints para gestión de inscripciones y lista de espera")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final UserServiceClient userServiceClient;

    /**
     * POST /api/registrations - Registrar usuario a evento
     */
    @PostMapping
    @Operation(summary = "Registrar usuario a evento", description = "Si hay cupo registra directo, si no agrega a lista de espera")
    public ResponseEntity<?> registerToEvent(
            @AuthenticationPrincipal String email,
            @Valid @RequestBody RegistrationRequest request) {
        try {
            String userId = getUserIdByEmail(email);
            RegistrationResponse response = registrationService.registerToEvent(userId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error al registrar usuario: {}", e.getMessage());
            return buildErrorResponse(e, "/api/registrations");
        }
    }

    /**
     * DELETE /api/registrations/{eventId} - Cancelar inscripción
     */
    @DeleteMapping("/{eventId}")
    @Operation(summary = "Cancelar inscripción", description = "Cancela la inscripción de un usuario en un evento")
    public ResponseEntity<?> cancelRegistration(
            @AuthenticationPrincipal String email,
            @PathVariable String eventId) {
        try {
            String userId = getUserIdByEmail(email);
            registrationService.cancelRegistration(userId, eventId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error al cancelar inscripción: {}", e.getMessage());
            return buildErrorResponse(e, "/api/registrations/" + eventId);
        }
    }

    /**
     * GET /api/registrations/user - Obtener inscripciones del usuario autenticado
     */
    @GetMapping("/user")
    @Operation(summary = "Mis inscripciones", description = "Obtiene todas las inscripciones del usuario autenticado")
    public ResponseEntity<List<RegistrationResponse>> getUserRegistrations(
            @AuthenticationPrincipal String email) {
        String userId = getUserIdByEmail(email);
        return ResponseEntity.ok(registrationService.getUserRegistrations(userId));
    }

    /**
     * GET /api/registrations/event/{eventId} - Obtener inscripciones de un evento (ADMIN/ENTRENADOR)
     */
    @GetMapping("/event/{eventId}")
    @Operation(summary = "Inscripciones por evento", description = "Obtiene todas las inscripciones de un evento específico")
    public ResponseEntity<List<RegistrationResponse>> getEventRegistrations(
            @PathVariable String eventId) {
        return ResponseEntity.ok(registrationService.getEventRegistrations(eventId));
    }

    /**
     * GET /api/registrations/check - Verificar si está registrado
     */
    @GetMapping("/check")
    @Operation(summary = "Verificar registro", description = "Verifica si el usuario está registrado en un evento")
    public ResponseEntity<Boolean> isUserRegistered(
            @AuthenticationPrincipal String email,
            @RequestParam String eventId) {
        String userId = getUserIdByEmail(email);
        return ResponseEntity.ok(registrationService.isUserRegistered(userId, eventId));
    }

    /**
     * GET /api/registrations/waitlist/position - Obtener posición en lista de espera
     */
    @GetMapping("/waitlist/position")
    @Operation(summary = "Posición en lista de espera", description = "Obtiene la posición actual en lista de espera")
    public ResponseEntity<Integer> getWaitlistPosition(
            @AuthenticationPrincipal String email,
            @RequestParam String eventId) {
        String userId = getUserIdByEmail(email);
        Integer position = registrationService.getWaitlistPosition(userId, eventId);
        return ResponseEntity.ok(position);
    }

    /**
     * POST /api/registrations/checkin/qr - Check-in mediante QR
     */
    @PostMapping("/checkin/qr")
    @Operation(summary = "Check-in con QR", description = "Registra asistencia escaneando código QR")
    public ResponseEntity<?> checkInByQr(
            @RequestParam String qrCode,
            @AuthenticationPrincipal String email) {
        try {
            String verifiedBy = getUserIdByEmail(email);
            registrationService.checkInByQr(qrCode, verifiedBy);
            return ResponseEntity.ok().body(Map.of("message", "Check-in exitoso"));
        } catch (Exception e) {
            log.error("Error en check-in QR: {}", e.getMessage());
            return buildErrorResponse(e, "/api/registrations/checkin/qr");
        }
    }

    /**
     * POST /api/registrations/checkin/manual - Check-in manual (ADMIN/ENTRENADOR)
     */
    @PostMapping("/checkin/manual")
    @Operation(summary = "Check-in manual", description = "Registra asistencia manualmente (solo ADMIN/ENTRENADOR)")
    public ResponseEntity<?> manualCheckIn(
            @RequestParam String userId,
            @RequestParam String eventId,
            @AuthenticationPrincipal String email) {
        try {
            String verifiedBy = getUserIdByEmail(email);
            registrationService.manualCheckIn(userId, eventId, verifiedBy);
            return ResponseEntity.ok().body(Map.of("message", "Check-in manual exitoso"));
        } catch (Exception e) {
            log.error("Error en check-in manual: {}", e.getMessage());
            return buildErrorResponse(e, "/api/registrations/checkin/manual");
        }
    }

    /**
     * GET /api/registrations/event/{eventId}/attendance - Obtener asistentes
     */
    @GetMapping("/event/{eventId}/attendance")
    @Operation(summary = "Asistentes del evento", description = "Obtiene la lista de usuarios que ya hicieron check-in")
    public ResponseEntity<List<RegistrationResponse>> getEventAttendees(
            @PathVariable String eventId) {
        return ResponseEntity.ok(registrationService.getEventAttendees(eventId));
    }
}