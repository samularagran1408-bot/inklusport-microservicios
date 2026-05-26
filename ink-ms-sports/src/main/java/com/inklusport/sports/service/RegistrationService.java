package com.inklusport.sports.service;

import com.inklusport.sports.dto.request.RegistrationRequest;
import com.inklusport.sports.dto.response.RegistrationResponse;
import com.inklusport.sports.entity.Event;
import com.inklusport.sports.entity.EventRegistration;
import com.inklusport.sports.repository.EventRegistrationRepository;
import com.inklusport.sports.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationService {

    private final EventRegistrationRepository registrationRepository;
    private final EventRepository eventRepository;

    @Transactional
    public RegistrationResponse registerToEvent(RegistrationRequest request) {
        Event event = eventRepository.findById(request.getEventId())
            .orElseThrow(() -> new IllegalArgumentException("Evento no encontrado"));

        if (registrationRepository.existsByEventIdAndUserId(request.getEventId(), request.getUserId())) {
            throw new IllegalStateException("El usuario ya se encuentra registrado.");
        }

        EventRegistration registration = new EventRegistration();
        registration.setId(UUID.randomUUID().toString());
        registration.setEventId(request.getEventId());
        registration.setUserId(request.getUserId());
        registration.setRegistrationDate(LocalDateTime.now());
        registration.setAttended(false);
        registration.setQrCode("QR_" + UUID.randomUUID().toString());

        String statusMessage; 

        if (event.getAvailableCapacity() > 0) {
            registration.setWaitlistPosition(null); 
            
            event.setAvailableCapacity(event.getAvailableCapacity() - 1);
            eventRepository.save(event);
            
            statusMessage = "Inscripción confirmada exitosamente. ¡Cupo asegurado!";
        } else {
            long personasEnEspera = registrationRepository.countByEventIdAndWaitlistPositionIsNotNull(request.getEventId());
            int nuevaPosicion = (int) personasEnEspera + 1;
            registration.setWaitlistPosition(nuevaPosicion);
            
            statusMessage = "El evento está lleno. Has sido agregado a la lista de espera en la posición: " + nuevaPosicion;
        }

        EventRegistration saved = registrationRepository.save(registration);
        
        return convertToResponse(saved, statusMessage, event.getName());
    }

    @Transactional
    public void cancelRegistration(String registrationId) {
        EventRegistration currentReg = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new IllegalArgumentException("Inscripción no encontrada"));

        String eventId = currentReg.getEventId();
        Integer posicionEliminada = currentReg.getWaitlistPosition();

        registrationRepository.delete(currentReg);

        if (posicionEliminada == null) {
            Optional<EventRegistration> nextInLine = registrationRepository
                    .findFirstByEventIdAndWaitlistPositionIsNotNullOrderByWaitlistPositionAsc(eventId);

            if (nextInLine.isPresent()) {
                EventRegistration promotedReg = nextInLine.get();
                
                promotedReg.setWaitlistPosition(null);
                registrationRepository.save(promotedReg);
                
                log.info("Usuario {} promovido automáticamente al evento.", promotedReg.getUserId());
                
                reorderWaitlist(eventId);
            } else {
                Event event = eventRepository.findById(eventId)
                        .orElseThrow(() -> new IllegalArgumentException("Evento no encontrado"));
                event.setAvailableCapacity(event.getAvailableCapacity() + 1);
                eventRepository.save(event);
            }
        } else {
            reorderWaitlist(eventId);
        }
    }

    private void reorderWaitlist(String eventId) {
        List<EventRegistration> waitlist = registrationRepository
                .findByEventIdAndWaitlistPositionIsNotNullOrderByWaitlistPositionAsc(eventId);
        
        int currentPosition = 1;
        for (EventRegistration reg : waitlist) {
            reg.setWaitlistPosition(currentPosition);
            registrationRepository.save(reg);
            currentPosition++;
        }
    }

    public List<RegistrationResponse> getWaitlistForEvent(String eventId) {
        /**
         * Trae a todos los que tienen waitlist_position NO nulo ordenados del 1 en adelante
         */
        List<EventRegistration> waitlist = registrationRepository
                .findByEventIdAndWaitlistPositionIsNotNullOrderByWaitlistPositionAsc(eventId);
        
        /**
         * Convierte a RegistrationResponse
         */
        return waitlist.stream()
                .map(reg -> convertToResponse(reg, "WAITLIST", "Nombre del Evento"))
                .toList();
    }

    private RegistrationResponse convertToResponse(EventRegistration reg, String statusMessage, String eventName) {
        return RegistrationResponse.builder()
                .id(reg.getId())
                .userId(reg.getUserId())
                .eventId(reg.getEventId())
                .eventName(eventName) 
                .qrCode(reg.getQrCode())
                .registrationDate(reg.getRegistrationDate())
                .attended(reg.getAttended())
                .waitlistPosition(reg.getWaitlistPosition())
                .message(statusMessage)
                .build();
    }
}