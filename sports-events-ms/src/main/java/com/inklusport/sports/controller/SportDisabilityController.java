package com.inklusport.sports.controller;

import com.inklusport.sports.dto.request.SportDisabilityRequest;
import com.inklusport.sports.dto.response.SportDisabilityResponse;
import com.inklusport.sports.dto.response.ErrorResponse;
import com.inklusport.sports.service.SportDisabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sport-disabilities")
@RequiredArgsConstructor
public class SportDisabilityController {

    private final SportDisabilityService sportDisabilityService;

    @PostMapping
    public ResponseEntity<?> addAssociation(@Valid @RequestBody SportDisabilityRequest request) {
        try {
            SportDisabilityResponse response = sportDisabilityService.addAssociation(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/sport-disabilities");
        }
    }

    @DeleteMapping("/sport/{sportId}/disability/{disabilityId}")
    public ResponseEntity<?> removeAssociation(@PathVariable Long sportId, @PathVariable Long disabilityId) {
        try {
            sportDisabilityService.removeAssociation(sportId, disabilityId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/sport-disabilities/sport/" + sportId + "/disability/" + disabilityId);
        }
    }

    @GetMapping("/sport/{sportId}")
    public ResponseEntity<List<SportDisabilityResponse>> getBySport(@PathVariable Long sportId) {
        return ResponseEntity.ok(sportDisabilityService.getAssociationsBySport(sportId));
    }

    @GetMapping("/disability/{disabilityId}")
    public ResponseEntity<List<SportDisabilityResponse>> getByDisability(@PathVariable Long disabilityId) {
        return ResponseEntity.ok(sportDisabilityService.getAssociationsByDisability(disabilityId));
    }

    @GetMapping("/adaptations")
    public ResponseEntity<?> getAdaptations(@RequestParam Long sportId, @RequestParam Long disabilityId) {
        try {
            String adaptations = sportDisabilityService.getAdaptations(sportId, disabilityId);
            return ResponseEntity.ok(adaptations);
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/sport-disabilities/adaptations");
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