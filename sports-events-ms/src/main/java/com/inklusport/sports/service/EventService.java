package com.inklusport.sports.service;

import com.inklusport.sports.dto.request.EventRequest;
import com.inklusport.sports.dto.response.EventResponse;
import com.inklusport.sports.dto.response.CalendarEventResponse;
import com.inklusport.sports.entity.Event;
import com.inklusport.sports.entity.Sport;
import com.inklusport.sports.repository.EventRepository;
import com.inklusport.sports.repository.EventRegistrationRepository;
import com.inklusport.sports.repository.SportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final EventRepository eventRepository;
    private final SportRepository sportRepository;
    private final EventRegistrationRepository registrationRepository;

    @Transactional
    public EventResponse createEvent(EventRequest request, String createdBy) {
        Sport sport = sportRepository.findById(request.getSportId())
                .orElseThrow(() -> new RuntimeException("Deporte no encontrado con ID: " + request.getSportId()));

        Event event = new Event();
        event.setSport(sport);
        event.setName(request.getName());
        event.setDescription(request.getDescription());
        event.setEventDate(request.getEventDate());
        event.setEventTime(request.getEventTime());
        event.setLocation(request.getLocation());
        event.setMaxCapacity(request.getMaxCapacity());
        event.setAvailableCapacity(request.getMaxCapacity());
        
        if (request.getStatus() != null) {
            event.setStatus(Event.EventStatus.valueOf(request.getStatus().toLowerCase()));
        }
        
        event.setCreatedBy(createdBy);

        Event savedEvent = eventRepository.save(event);
        log.info("Evento creado: {}", savedEvent.getName());

        return convertToResponse(savedEvent);
    }

    @Transactional(readOnly = true)
    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EventResponse> getActiveEvents() {
        return eventRepository.findByStatus(Event.EventStatus.active).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CalendarEventResponse> getUpcomingEventsForCalendar() {
        LocalDate today = LocalDate.now();
        return eventRepository.findUpcomingEvents(today).stream()
                .map(this::convertToCalendarResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CalendarEventResponse> getEventsByDateRange(LocalDate startDate, LocalDate endDate) {
        return eventRepository.findEventsInDateRange(startDate, endDate).stream()
                .map(this::convertToCalendarResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EventResponse> getEventsByFilters(LocalDate date, Long sportId, String location) {
        List<Event> events = eventRepository.findByStatus(Event.EventStatus.active);
        
        if (date != null) {
            events = events.stream()
                    .filter(e -> e.getEventDate().equals(date))
                    .collect(Collectors.toList());
        }
        
        if (sportId != null) {
            events = events.stream()
                    .filter(e -> e.getSport().getId().equals(sportId))
                    .collect(Collectors.toList());
        }
        
        if (location != null && !location.isEmpty()) {
            events = events.stream()
                    .filter(e -> e.getLocation() != null && e.getLocation().toLowerCase().contains(location.toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        return events.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EventResponse getEventById(String id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con ID: " + id));
        return convertToResponse(event);
    }

    @Transactional
    public EventResponse updateEvent(String id, EventRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con ID: " + id));

        if (request.getSportId() != null && !request.getSportId().equals(event.getSport().getId())) {
            Sport sport = sportRepository.findById(request.getSportId())
                    .orElseThrow(() -> new RuntimeException("Deporte no encontrado"));
            event.setSport(sport);
        }

        if (request.getName() != null) {
            event.setName(request.getName());
        }

        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }

        if (request.getEventDate() != null) {
            event.setEventDate(request.getEventDate());
        }

        if (request.getEventTime() != null) {
            event.setEventTime(request.getEventTime());
        }

        if (request.getLocation() != null) {
            event.setLocation(request.getLocation());
        }

        if (request.getMaxCapacity() != null) {
            int oldCapacity = event.getMaxCapacity();
            int capacityDiff = request.getMaxCapacity() - oldCapacity;
            event.setMaxCapacity(request.getMaxCapacity());
            event.setAvailableCapacity(event.getAvailableCapacity() + capacityDiff);
        }

        if (request.getStatus() != null) {
            event.setStatus(Event.EventStatus.valueOf(request.getStatus().toLowerCase()));
        }

        Event updatedEvent = eventRepository.save(event);
        log.info("Evento actualizado: {}", updatedEvent.getName());

        return convertToResponse(updatedEvent);
    }

    @Transactional
    public void deleteEvent(String id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con ID: " + id));
        
        eventRepository.delete(event);
        log.info("Evento eliminado: {}", event.getName());
    }

    private EventResponse convertToResponse(Event event) {
        long registeredCount = registrationRepository.countByEventId(event.getId());
        
        return EventResponse.builder()
                .id(event.getId())
                .sportId(event.getSport().getId())
                .sportName(event.getSport().getName())
                .name(event.getName())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .eventTime(event.getEventTime())
                .location(event.getLocation())
                .maxCapacity(event.getMaxCapacity())
                .availableCapacity(event.getAvailableCapacity())
                .status(event.getStatus().toString())
                .createdBy(event.getCreatedBy())
                .createdAt(event.getCreatedAt())
                .registeredCount(registeredCount)
                .build();
    }

    private CalendarEventResponse convertToCalendarResponse(Event event) {
        return CalendarEventResponse.builder()
                .id(event.getId())
                .title(event.getName())
                .startDate(event.getEventDate())
                .startTime(event.getEventTime())
                .location(event.getLocation())
                .sportName(event.getSport().getName())
                .availableCapacity(event.getAvailableCapacity())
                .maxCapacity(event.getMaxCapacity())
                .build();
    }
}