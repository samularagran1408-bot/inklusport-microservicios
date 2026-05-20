package com.inklusport.sports.controller;

import com.inklusport.sports.dto.request.RegistrationRequest;
import com.inklusport.sports.dto.request.WaitlistRequest;
import com.inklusport.sports.dto.response.RegistrationResponse;
import com.inklusport.sports.dto.response.ErrorResponse;
import com.inklusport.sports.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/registrations")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    /**
     * Registrar usuario a evento (va directo o a lista de espera según cupos)
     * POST /api/registrations
     */
    @PostMapping
    public ResponseEntity<?> registerToEvent(@RequestHeader("X-User-Id") String userId,
                                              @Valid @RequestBody RegistrationRequest request) {
        try {
            RegistrationResponse response = registrationService.registerToEvent(userId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/registrations");
        }
    }

    /**
     * Agregar usuario explícitamente a lista de espera (usa WaitlistRequest)
     * POST /api/registrations/waitlist
     */
    @PostMapping("/waitlist")
    public ResponseEntity<?> addToWaitlist(@RequestHeader("X-User-Id") String userId,
                                            @Valid @RequestBody WaitlistRequest request) {
        try {
            RegistrationResponse response = registrationService.addToWaitlist(userId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/registrations/waitlist");
        }
    }

    /**
     * Cancelar inscripción
     * DELETE /api/registrations/{eventId}
     */
    @DeleteMapping("/{eventId}")
    public ResponseEntity<?> cancelRegistration(@RequestHeader("X-User-Id") String userId,
                                                 @PathVariable String eventId) {
        try {
            registrationService.cancelRegistration(userId, eventId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/registrations/" + eventId);
        }
    }

    /**
     * Check-in mediante QR
     * POST /api/registrations/checkin
     */
    @PostMapping("/checkin")
    public ResponseEntity<?> checkIn(@RequestParam String qrCode,
                                      @RequestHeader(value = "X-User-Id", required = false) String verifiedBy) {
        try {
            registrationService.checkIn(qrCode, verifiedBy);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/registrations/checkin");
        }
    }

    /**
     * Check-in manual (admin/entrenador)
     * POST /api/registrations/checkin/manual
     */
    @PostMapping("/checkin/manual")
    public ResponseEntity<?> manualCheckIn(@RequestParam String userId,
                                            @RequestParam String eventId,
                                            @RequestHeader("X-User-Id") String verifiedBy) {
        try {
            registrationService.manualCheckIn(userId, eventId, verifiedBy);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/registrations/checkin/manual");
        }
    }

    /**
     * Obtener inscripciones del usuario
     * GET /api/registrations/user
     */
    @GetMapping("/user")
    public ResponseEntity<List<RegistrationResponse>> getUserRegistrations(@RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(registrationService.getUserRegistrations(userId));
    }

    /**
     * Obtener inscripciones de un evento
     * GET /api/registrations/event/{eventId}
     */
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<RegistrationResponse>> getEventRegistrations(@PathVariable String eventId) {
        return ResponseEntity.ok(registrationService.getEventRegistrations(eventId));
    }

    /**
     * Obtener lista de espera de un evento
     * GET /api/registrations/event/{eventId}/waitlist
     */
    @GetMapping("/event/{eventId}/waitlist")
    public ResponseEntity<?> getEventWaitlist(@PathVariable String eventId) {
        return ResponseEntity.ok(registrationService.getEventWaitlist(eventId));
    }

    /**
     * Verificar si usuario está registrado
     * GET /api/registrations/check?eventId={eventId}
     */
    @GetMapping("/check")
    public ResponseEntity<Boolean> isUserRegistered(@RequestHeader("X-User-Id") String userId,
                                                      @RequestParam String eventId) {
        return ResponseEntity.ok(registrationService.isUserRegistered(userId, eventId));
    }

    /**
     * Aceptar oferta de lista de espera
     * POST /api/registrations/waitlist/accept
     */
    @PostMapping("/waitlist/accept")
    public ResponseEntity<?> acceptWaitlistOffer(@RequestHeader("X-User-Id") String userId,
                                                  @RequestParam String eventId) {
        try {
            registrationService.acceptWaitlistOffer(userId, eventId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/registrations/waitlist/accept");
        }
    }

    /**
     * Rechazar oferta de lista de espera
     * POST /api/registrations/waitlist/reject
     */
    @PostMapping("/waitlist/reject")
    public ResponseEntity<?> rejectWaitlistOffer(@RequestHeader("X-User-Id") String userId,
                                                  @RequestParam String eventId) {
        try {
            registrationService.rejectWaitlistOffer(userId, eventId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/registrations/waitlist/reject");
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