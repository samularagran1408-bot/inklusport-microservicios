package com.inklusport.sports.controller;

import com.inklusport.sports.dto.SportDisabilityRequest;
import com.inklusport.sports.dto.SportDisabilityResponse;
import com.inklusport.sports.service.SportDisabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sport-disabilities")
@RequiredArgsConstructor
public class SportDisabilityController {

    private final SportDisabilityService sportDisabilityService;

    @GetMapping("/sport/{sportId}")
    public ResponseEntity<List<SportDisabilityResponse>> getSportDisabilities(@PathVariable Integer sportId) {
        return ResponseEntity.ok(sportDisabilityService.getSportDisabilities(sportId));
    }

    @PostMapping
    public ResponseEntity<SportDisabilityResponse> addAdaptation(@RequestBody SportDisabilityRequest request) {
        return ResponseEntity.ok(sportDisabilityService.addAdaptation(request));
    }

    @PutMapping("/sport/{sportId}/disability/{disabilityId}")
    public ResponseEntity<SportDisabilityResponse> updateAdaptation(
            @PathVariable Integer sportId,
            @PathVariable Integer disabilityId,
            @RequestBody SportDisabilityRequest request) {
        return ResponseEntity.ok(sportDisabilityService.updateAdaptation(sportId, disabilityId, request));
    }

    @DeleteMapping("/sport/{sportId}/disability/{disabilityId}")
    public ResponseEntity<Void> removeAdaptation(
            @PathVariable Integer sportId,
            @PathVariable Integer disabilityId) {
        sportDisabilityService.removeAdaptation(sportId, disabilityId);
        return ResponseEntity.noContent().build();
    }
}