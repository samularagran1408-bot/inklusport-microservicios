package com.inklusport.sports.controller;

import com.inklusport.sports.dto.SportRequest;
import com.inklusport.sports.dto.SportResponse;
import com.inklusport.sports.service.SportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/sports")
@PreAuthorize("hasRole('ADMIN') or hasRole('COACH')")
@RequiredArgsConstructor
public class SportController {

    private final SportService sportService;

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<SportResponse>> getAllSports() {
        return ResponseEntity.ok(sportService.getAllSports());
    }

    @GetMapping("/active")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<SportResponse>> getActiveSports() {
        return ResponseEntity.ok(sportService.getActiveSports());
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<SportResponse> getSportById(@PathVariable Integer id) {
        return ResponseEntity.ok(sportService.getSportById(id));
    }

    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<SportResponse> createSport(@RequestBody SportRequest request) {
        return ResponseEntity.ok(sportService.createSport(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<SportResponse> updateSport(@PathVariable Integer id, @RequestBody SportRequest request) {
        return ResponseEntity.ok(sportService.updateSport(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Void> deleteSport(@PathVariable Integer id) {
        sportService.deleteSport(id);
        return ResponseEntity.noContent().build();
    }
}