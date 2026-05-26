package com.inklusport.sports.controller;

import com.inklusport.sports.dto.request.RegistrationRequest;
import com.inklusport.sports.dto.response.RegistrationResponse;
import com.inklusport.sports.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/registrations")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public ResponseEntity<RegistrationResponse> registerToEvent(@RequestBody RegistrationRequest request) {
        return ResponseEntity.ok(registrationService.registerToEvent(request));
    }
}