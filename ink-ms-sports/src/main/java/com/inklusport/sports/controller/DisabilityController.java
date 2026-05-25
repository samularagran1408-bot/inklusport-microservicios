package com.inklusport.sports.controller;

import com.inklusport.sports.dto.request.DisabilityRequest;
import com.inklusport.sports.dto.response.DisabilityResponse;
import com.inklusport.sports.dto.response.ErrorResponse;
import com.inklusport.sports.service.DisabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/disabilities")
@RequiredArgsConstructor
public class DisabilityController {

    private final DisabilityService disabilityService;

    @PostMapping
    public ResponseEntity<?> createDisability(@Valid @RequestBody DisabilityRequest request) {
        try {
            DisabilityResponse response = disabilityService.createDisability(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/disabilities");
        }
    }

    @GetMapping
    public ResponseEntity<List<DisabilityResponse>> getAllDisabilities() {
        return ResponseEntity.ok(disabilityService.getAllDisabilities());
    }

    @GetMapping("/active")
    public ResponseEntity<List<DisabilityResponse>> getActiveDisabilities() {
        return ResponseEntity.ok(disabilityService.getActiveDisabilities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDisabilityById(@PathVariable Long id) {
        try {
            DisabilityResponse response = disabilityService.getDisabilityById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/disabilities/" + id);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDisability(@PathVariable Long id, @Valid @RequestBody DisabilityRequest request) {
        try {
            DisabilityResponse response = disabilityService.updateDisability(id, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/disabilities/" + id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDisability(@PathVariable Long id) {
        try {
            disabilityService.deleteDisability(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/disabilities/" + id);
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