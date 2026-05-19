package com.inklusport.sports.controller;

import com.inklusport.sports.dto.request.RegistrationRequest;
import com.inklusport.sports.dto.request.WaitlistRequest;
import com.inklusport.sports.dto.response.RegistrationResponse;
import com.inklusport.sports.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registrations")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<RegistrationResponse>> findByEvent(@PathVariable String eventId) {
        return ResponseEntity.ok(registrationService.findByEvent(eventId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RegistrationResponse>> findByUser(@PathVariable String userId) {
        return ResponseEntity.ok(registrationService.findByUser(userId));
    }

    @PostMapping
    public ResponseEntity<RegistrationResponse> register(@Valid @RequestBody RegistrationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registrationService.register(request));
    }

    @PostMapping("/waitlist")
    public ResponseEntity<RegistrationResponse> addToWaitlist(@Valid @RequestBody WaitlistRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registrationService.addToWaitlist(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable String id) {
        registrationService.cancelRegistration(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/attendance")
    public ResponseEntity<RegistrationResponse> markAttendance(@PathVariable String id) {
        return ResponseEntity.ok(registrationService.markAttendance(id));
    }
}
