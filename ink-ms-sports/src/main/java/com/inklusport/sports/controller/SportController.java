package com.inklusport.sports.controller;

import com.inklusport.sports.dto.request.SportRequest;
import com.inklusport.sports.dto.response.SportResponse;
import com.inklusport.sports.dto.response.ErrorResponse;
import com.inklusport.sports.service.SportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sports")
@RequiredArgsConstructor
public class SportController {

    private final SportService sportService;

    /**
     * Crear un nuevo deporte
     * POST /api/sports
     */
    @PostMapping
    public ResponseEntity<?> createSport(@Valid @RequestBody SportRequest request) {
        try {
            SportResponse response = sportService.createSport(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/sports");
        }
    }

    /**
     * Obtener todos los deportes
     * GET /api/sports
     */
    @GetMapping
    public ResponseEntity<List<SportResponse>> getAllSports() {
        return ResponseEntity.ok(sportService.getAllSports());
    }

    /**
     * Obtener deportes activos
     * GET /api/sports/active
     */
    @GetMapping("/active")
    public ResponseEntity<List<SportResponse>> getActiveSports() {
        return ResponseEntity.ok(sportService.getActiveSports());
    }

    /**
     * Obtener deportes por discapacidad
     * GET /api/sports/disability/{disabilityId}
     */
    @GetMapping("/disability/{disabilityId}")
    public ResponseEntity<List<SportResponse>> getSportsByDisability(@PathVariable Long disabilityId) {
        return ResponseEntity.ok(sportService.getSportsByDisability(disabilityId));
    }

    /**
     * Obtener deporte por ID
     * GET /api/sports/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getSportById(@PathVariable Long id) {
        try {
            SportResponse response = sportService.getSportById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/sports/" + id);
        }
    }

    /**
     * Actualizar deporte
     * PUT /api/sports/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSport(@PathVariable Long id, @Valid @RequestBody SportRequest request) {
        try {
            SportResponse response = sportService.updateSport(id, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/sports/" + id);
        }
    }

    /**
     * Eliminar deporte
     * DELETE /api/sports/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSport(@PathVariable Long id) {
        try {
            sportService.deleteSport(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/sports/" + id);
        }
    }

    /**
     * Activar deporte
     * PATCH /api/sports/{id}/activate
     */
    @PatchMapping("/{id}/activate")
    public ResponseEntity<?> activateSport(@PathVariable Long id) {
        try {
            sportService.activateSport(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/sports/" + id + "/activate");
        }
    }

    /**
     * Desactivar deporte
     * PATCH /api/sports/{id}/deactivate
     */
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateSport(@PathVariable Long id) {
        try {
            sportService.deactivateSport(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/sports/" + id + "/deactivate");
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