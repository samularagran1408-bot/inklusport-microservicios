package com.inklusport.sports.service;

import com.inklusport.sports.client.UserServiceClient;
import com.inklusport.sports.dto.request.RegistrationRequest;
import com.inklusport.sports.dto.request.WaitlistRequest;
import com.inklusport.sports.dto.response.RegistrationResponse;
import com.inklusport.sports.entity.Event;
import com.inklusport.sports.entity.EventAttendance;
import com.inklusport.sports.entity.EventRegistration;
import com.inklusport.sports.entity.Waitlist;
import com.inklusport.sports.repository.EventAttendanceRepository;
import com.inklusport.sports.repository.EventRegistrationRepository;
import com.inklusport.sports.repository.EventRepository;
import com.inklusport.sports.repository.WaitlistRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private static final String CONFIRMED = "CONFIRMED";

    private final EventRepository eventRepository;
    private final EventRegistrationRepository registrationRepository;
    private final EventAttendanceRepository attendanceRepository;
    private final WaitlistRepository waitlistRepository;
    private final UserServiceClient userServiceClient;

    @Transactional(readOnly = true)
    public List<RegistrationResponse> findByEvent(String eventId) {
        return registrationRepository.findByEvent_Id(eventId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RegistrationResponse> findByUser(String userId) {
        return registrationRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public RegistrationResponse register(RegistrationRequest request) {
        validateUserExists(request.getUserId());
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        if (registrationRepository.existsByEvent_IdAndUserId(event.getId(), request.getUserId())) {
            throw new RuntimeException("El usuario ya está inscrito en este evento");
        }
        if (waitlistRepository.existsByEvent_IdAndUserId(event.getId(), request.getUserId())) {
            throw new RuntimeException("El usuario ya está en lista de espera");
        }

        long confirmed = registrationRepository.countByEvent_IdAndStatus(event.getId(), CONFIRMED);
        if (confirmed >= event.getMaxParticipants()) {
            throw new RuntimeException("El evento está completo. Use la lista de espera.");
        }

        EventRegistration registration = new EventRegistration();
        registration.setEvent(event);
        registration.setUserId(request.getUserId());
        registration.setStatus(CONFIRMED);
        return toResponse(registrationRepository.save(registration));
    }

    @Transactional
    public RegistrationResponse addToWaitlist(WaitlistRequest request) {
        validateUserExists(request.getUserId());
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        if (registrationRepository.existsByEvent_IdAndUserId(event.getId(), request.getUserId())) {
            throw new RuntimeException("El usuario ya está inscrito en este evento");
        }
        if (waitlistRepository.existsByEvent_IdAndUserId(event.getId(), request.getUserId())) {
            throw new RuntimeException("El usuario ya está en lista de espera");
        }

        int position = (int) waitlistRepository.countByEvent_Id(event.getId()) + 1;
        Waitlist entry = new Waitlist();
        entry.setEvent(event);
        entry.setUserId(request.getUserId());
        entry.setPosition(position);
        waitlistRepository.save(entry);

        return RegistrationResponse.builder()
                .eventId(event.getId())
                .userId(request.getUserId())
                .status("WAITLIST")
                .onWaitlist(true)
                .waitlistPosition(position)
                .build();
    }

    @Transactional
    public void cancelRegistration(String registrationId) {
        EventRegistration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));
        registration.setStatus("CANCELLED");
        registrationRepository.save(registration);
        promoteFromWaitlist(registration.getEvent());
    }

    @Transactional
    public RegistrationResponse markAttendance(String registrationId) {
        EventRegistration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));
        if (!CONFIRMED.equals(registration.getStatus())) {
            throw new RuntimeException("Solo inscripciones confirmadas pueden registrar asistencia");
        }
        if (attendanceRepository.findByRegistration_Id(registrationId).isPresent()) {
            throw new RuntimeException("La asistencia ya fue registrada");
        }
        EventAttendance attendance = new EventAttendance();
        attendance.setRegistration(registration);
        attendanceRepository.save(attendance);
        return toResponse(registration);
    }

    private void promoteFromWaitlist(Event event) {
        List<Waitlist> waitlist = waitlistRepository.findByEvent_IdOrderByPositionAsc(event.getId());
        if (waitlist.isEmpty()) {
            return;
        }
        long confirmed = registrationRepository.countByEvent_IdAndStatus(event.getId(), CONFIRMED);
        if (confirmed >= event.getMaxParticipants()) {
            return;
        }
        Waitlist next = waitlist.get(0);
        EventRegistration registration = new EventRegistration();
        registration.setEvent(event);
        registration.setUserId(next.getUserId());
        registration.setStatus(CONFIRMED);
        registrationRepository.save(registration);
        waitlistRepository.delete(next);
    }

    private void validateUserExists(String userId) {
        try {
            userServiceClient.getUserById(userId);
        } catch (FeignException.NotFound e) {
            throw new RuntimeException("Usuario no encontrado");
        } catch (FeignException e) {
            throw new RuntimeException("No se pudo validar el usuario con users-ms");
        }
    }

    private RegistrationResponse toResponse(EventRegistration registration) {
        boolean onWaitlist = waitlistRepository
                .findByEvent_IdAndUserId(registration.getEvent().getId(), registration.getUserId())
                .isPresent();
        Integer position = waitlistRepository
                .findByEvent_IdAndUserId(registration.getEvent().getId(), registration.getUserId())
                .map(Waitlist::getPosition)
                .orElse(null);

        return RegistrationResponse.builder()
                .id(registration.getId())
                .eventId(registration.getEvent().getId())
                .userId(registration.getUserId())
                .status(registration.getStatus())
                .registeredAt(registration.getRegisteredAt())
                .onWaitlist(onWaitlist)
                .waitlistPosition(position)
                .build();
    }
}
