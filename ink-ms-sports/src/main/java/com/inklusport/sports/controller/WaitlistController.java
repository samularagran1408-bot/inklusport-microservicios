package com.inklusport.sports.controller;

import com.inklusport.sports.dto.request.WaitlistRequest;
import com.inklusport.sports.dto.response.WaitlistResponse;
import com.inklusport.sports.service.WaitlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/waitlist")
@RequiredArgsConstructor
public class WaitlistController {

    private final WaitlistService waitlistService;

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<WaitlistResponse>> getWaitlistByEvent(@PathVariable String eventId) {
        return ResponseEntity.ok(waitlistService.getWaitlistByEvent(eventId));
    }

    @PostMapping
    public ResponseEntity<WaitlistResponse> addToWaitlist(@RequestBody WaitlistRequest request) {
        return ResponseEntity.ok(waitlistService.addToWaitlist(request));
    }
}