package com.inklusport.sports.controller;

import com.inklusport.sports.dto.request.EventRequest;
import com.inklusport.sports.dto.response.CalendarEventResponse;
import com.inklusport.sports.dto.response.EventResponse;
import com.inklusport.sports.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventResponse>> findAll() {
        return ResponseEntity.ok(eventService.findAll());
    }

    @GetMapping("/calendar")
    public ResponseEntity<List<CalendarEventResponse>> getCalendar(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(eventService.getCalendar(from, to));
    }

    @GetMapping("/sport/{sportId}")
    public ResponseEntity<List<EventResponse>> findBySport(@PathVariable String sportId) {
        return ResponseEntity.ok(eventService.findBySport(sportId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(eventService.findById(id));
    }

    @PostMapping
    public ResponseEntity<EventResponse> create(@Valid @RequestBody EventRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> update(@PathVariable String id, @Valid @RequestBody EventRequest request) {
        return ResponseEntity.ok(eventService.update(id, request));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<EventResponse> cancel(@PathVariable String id) {
        return ResponseEntity.ok(eventService.cancel(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
