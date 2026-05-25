package com.inklusport.sports.controller;

import com.inklusport.sports.dto.request.SportRequest;
import com.inklusport.sports.dto.response.SportResponse;
import com.inklusport.sports.service.SportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sports")
@RequiredArgsConstructor
public class SportController {

    private final SportService sportService;

    @GetMapping
    public ResponseEntity<List<SportResponse>> getAllSports() {
        return ResponseEntity.ok(sportService.getAllSports());
    }

    @GetMapping("/active")
    public ResponseEntity<List<SportResponse>> getActiveSports() {
        return ResponseEntity.ok(sportService.getActiveSports());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SportResponse> getSportById(@PathVariable Integer id) {
        return ResponseEntity.ok(sportService.getSportById(id));
    }

    @PostMapping
    public ResponseEntity<SportResponse> createSport(@RequestBody SportRequest request) {
        return ResponseEntity.ok(sportService.createSport(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SportResponse> updateSport(@PathVariable Integer id, @RequestBody SportRequest request) {
        return ResponseEntity.ok(sportService.updateSport(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSport(@PathVariable Integer id) {
        sportService.deleteSport(id);
        return ResponseEntity.noContent().build();
    }
}