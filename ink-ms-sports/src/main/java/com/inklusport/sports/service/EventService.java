package com.inklusport.sports.service;

import com.inklusport.sports.dto.request.EventRequest;
import com.inklusport.sports.dto.response.EventResponse;
import com.inklusport.sports.entity.Event;
import com.inklusport.sports.entity.Sport;
import com.inklusport.sports.repository.EventRepository;
import com.inklusport.sports.repository.SportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final SportRepository sportRepository;

    @Transactional(readOnly = true)
    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll().stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Transactional
    public EventResponse createEvent(EventRequest request) {
        Sport sport = sportRepository.findById(request.getSportId())
                .orElseThrow(() -> new RuntimeException("Deporte no encontrado"));
        Event event = Event.builder()
                .sportId(sport.getId()).name(request.getName()).description(request.getDescription())
                .eventDate(request.getEventDate()).eventTime(request.getEventTime()).location(request.getLocation())
                .maxCapacity(request.getMaxCapacity()).createdBy(request.getCreatedBy())
                .build();
        return convertToResponse(eventRepository.save(event));
    }

    private EventResponse convertToResponse(Event event) {
        Sport sport = sportRepository.findById(event.getSportId()).orElse(null);
        return EventResponse.builder()
                .id(event.getId()).sportId(event.getSportId()).sportName(sport != null ? sport.getName() : "N/A")
                .name(event.getName()).description(event.getDescription()).eventDate(event.getEventDate())
                .eventTime(event.getEventTime()).location(event.getLocation()).maxCapacity(event.getMaxCapacity())
                .availableCapacity(event.getAvailableCapacity()).status(event.getStatus().name())
                .createdAt(event.getCreatedAt()).build();
    }
}