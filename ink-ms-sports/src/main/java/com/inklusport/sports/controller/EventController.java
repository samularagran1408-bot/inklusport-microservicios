package com.inklusport.sports.controller;

import com.inklusport.sports.dto.request.EventRequest;
import com.inklusport.sports.dto.response.EventResponse;
import com.inklusport.sports.dto.response.CalendarEventResponse;
import com.inklusport.sports.dto.response.ErrorResponse;
import com.inklusport.sports.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<?> createEvent(@Valid @RequestBody EventRequest request,
                                          @RequestHeader(value = "X-User-Id", required = false) String userId) {
        try {
            String createdBy = userId != null ? userId : "system";
            EventResponse response = eventService.createEvent(request, createdBy);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/events");
        }
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/active")
    public ResponseEntity<List<EventResponse>> getActiveEvents() {
        return ResponseEntity.ok(eventService.getActiveEvents());
    }

    @GetMapping("/calendar")
    public ResponseEntity<List<CalendarEventResponse>> getCalendarEvents() {
        return ResponseEntity.ok(eventService.getUpcomingEventsForCalendar());
    }

    @GetMapping("/calendar/range")
    public ResponseEntity<List<CalendarEventResponse>> getEventsByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(eventService.getEventsByDateRange(startDate, endDate));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<EventResponse>> getEventsByFilters(
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) Long sportId,
            @RequestParam(required = false) String location) {
        return ResponseEntity.ok(eventService.getEventsByFilters(date, sportId, location));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@PathVariable String id) {
        try {
            EventResponse response = eventService.getEventById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/events/" + id);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable String id, @Valid @RequestBody EventRequest request) {
        try {
            EventResponse response = eventService.updateEvent(id, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/events/" + id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable String id) {
        try {
            eventService.deleteEvent(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/events/" + id);
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