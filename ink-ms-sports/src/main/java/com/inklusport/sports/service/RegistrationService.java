package com.inklusport.sports.service;

import com.inklusport.sports.dto.request.RegistrationRequest;
import com.inklusport.sports.dto.response.RegistrationResponse;
import com.inklusport.sports.entity.Event;
import com.inklusport.sports.entity.EventRegistration;
import com.inklusport.sports.repository.EventRegistrationRepository;
import com.inklusport.sports.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final EventRegistrationRepository registrationRepository;
    private final EventRepository eventRepository;

    @Transactional
    public RegistrationResponse registerToEvent(RegistrationRequest request) {
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        if (event.getAvailableCapacity() <= 0) {
            throw new RuntimeException("No hay capacidad. Entrarías a lista de espera.");
        }

        event.setAvailableCapacity(event.getAvailableCapacity() - 1);
        eventRepository.save(event);

        EventRegistration reg = EventRegistration.builder()
                .userId(request.getUserId()).eventId(event.getId()).attended(false)
                .qrCode("QR_" + UUID.randomUUID()).build();

        return convertToResponse(registrationRepository.save(reg));
    }

    private RegistrationResponse convertToResponse(EventRegistration reg) {
        return RegistrationResponse.builder()
                .id(reg.getId()).userId(reg.getUserId()).eventId(reg.getEventId())
                .registrationDate(reg.getRegistrationDate()).attended(reg.getAttended())
                .waitlistPosition(reg.getWaitlistPosition()).qrCode(reg.getQrCode()).build();
    }
}