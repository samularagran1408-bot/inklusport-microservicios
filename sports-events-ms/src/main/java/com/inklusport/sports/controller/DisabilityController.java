package com.inklusport.sports.controller;

import com.inklusport.sports.dto.request.DisabilityRequest;
import com.inklusport.sports.dto.response.DisabilityResponse;
import com.inklusport.sports.service.DisabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disabilities")
@RequiredArgsConstructor
public class DisabilityController {

    private final DisabilityService disabilityService;

    @GetMapping
    public ResponseEntity<List<DisabilityResponse>> findAll() {
        return ResponseEntity.ok(disabilityService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisabilityResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(disabilityService.findById(id));
    }

    @PostMapping
    public ResponseEntity<DisabilityResponse> create(@Valid @RequestBody DisabilityRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(disabilityService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisabilityResponse> update(
            @PathVariable String id,
            @Valid @RequestBody DisabilityRequest request) {
        return ResponseEntity.ok(disabilityService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        disabilityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
