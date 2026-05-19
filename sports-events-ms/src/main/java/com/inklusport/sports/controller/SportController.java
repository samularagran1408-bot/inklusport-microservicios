package com.inklusport.sports.controller;

import com.inklusport.sports.dto.request.SportRequest;
import com.inklusport.sports.dto.response.SportResponse;
import com.inklusport.sports.service.SportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sports")
@RequiredArgsConstructor
public class SportController {

    private final SportService sportService;

    @GetMapping
    public ResponseEntity<List<SportResponse>> findAll() {
        return ResponseEntity.ok(sportService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SportResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(sportService.findById(id));
    }

    @PostMapping
    public ResponseEntity<SportResponse> create(@Valid @RequestBody SportRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sportService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SportResponse> update(@PathVariable String id, @Valid @RequestBody SportRequest request) {
        return ResponseEntity.ok(sportService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        sportService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{sportId}/disabilities/{disabilityId}")
    public ResponseEntity<SportResponse> linkDisability(
            @PathVariable String sportId,
            @PathVariable String disabilityId) {
        return ResponseEntity.ok(sportService.linkDisability(sportId, disabilityId));
    }
}
