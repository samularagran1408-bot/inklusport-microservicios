package com.inklusport.sports.service;

import com.inklusport.sports.dto.request.EventRequest;
import com.inklusport.sports.dto.response.CalendarEventResponse;
import com.inklusport.sports.dto.response.EventResponse;
import com.inklusport.sports.entity.Event;
import com.inklusport.sports.entity.Sport;
import com.inklusport.sports.repository.EventRegistrationRepository;
import com.inklusport.sports.repository.EventRepository;
import com.inklusport.sports.repository.SportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final SportRepository sportRepository;
    private final EventRegistrationRepository registrationRepository;

    @Transactional(readOnly = true)
    public List<EventResponse> findAll() {
        return eventRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public EventResponse findById(String id) {
        return toResponse(getById(id));
    }

    @Transactional(readOnly = true)
    public List<EventResponse> findBySport(String sportId) {
        return eventRepository.findBySport_Id(sportId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CalendarEventResponse> getCalendar(LocalDateTime from, LocalDateTime to) {
        return eventRepository.findByStartDateBetween(from, to).stream()
                .map(event -> CalendarEventResponse.builder()
                        .id(event.getId())
                        .title(event.getTitle())
                        .sportName(event.getSport().getName())
                        .location(event.getLocation())
                        .startDate(event.getStartDate())
                        .endDate(event.getEndDate())
                        .status(event.getStatus())
                        .build())
                .toList();
    }

    @Transactional
    public EventResponse create(EventRequest request) {
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new RuntimeException("La fecha de fin debe ser posterior a la de inicio");
        }
        Sport sport = sportRepository.findById(request.getSportId())
                .orElseThrow(() -> new RuntimeException("Deporte no encontrado"));

        Event event = new Event();
        event.setSport(sport);
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setLocation(request.getLocation());
        event.setStartDate(request.getStartDate());
        event.setEndDate(request.getEndDate());
        event.setMaxParticipants(request.getMaxParticipants());
        return toResponse(eventRepository.save(event));
    }

    @Transactional
    public EventResponse update(String id, EventRequest request) {
        Event event = getById(id);
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new RuntimeException("La fecha de fin debe ser posterior a la de inicio");
        }
        Sport sport = sportRepository.findById(request.getSportId())
                .orElseThrow(() -> new RuntimeException("Deporte no encontrado"));
        event.setSport(sport);
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setLocation(request.getLocation());
        event.setStartDate(request.getStartDate());
        event.setEndDate(request.getEndDate());
        event.setMaxParticipants(request.getMaxParticipants());
        return toResponse(eventRepository.save(event));
    }

    @Transactional
    public void delete(String id) {
        if (!eventRepository.existsById(id)) {
            throw new RuntimeException("Evento no encontrado");
        }
        eventRepository.deleteById(id);
    }

    @Transactional
    public EventResponse cancel(String id) {
        Event event = getById(id);
        event.setStatus("CANCELLED");
        return toResponse(eventRepository.save(event));
    }

    private Event getById(String id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));
    }

    private EventResponse toResponse(Event event) {
        long registered = registrationRepository.countByEvent_IdAndStatus(event.getId(), "CONFIRMED");
        return EventResponse.builder()
                .id(event.getId())
                .sportId(event.getSport().getId())
                .sportName(event.getSport().getName())
                .title(event.getTitle())
                .description(event.getDescription())
                .location(event.getLocation())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .maxParticipants(event.getMaxParticipants())
                .registeredCount(registered)
                .status(event.getStatus())
                .createdAt(event.getCreatedAt())
                .build();
    }
}
