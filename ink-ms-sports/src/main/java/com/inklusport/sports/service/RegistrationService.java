package com.inklusport.sports.service;

import com.inklusport.sports.dto.request.RegistrationRequest;
import com.inklusport.sports.dto.request.WaitlistRequest;
import com.inklusport.sports.dto.response.RegistrationResponse;
import com.inklusport.sports.dto.response.WaitlistResponse;
import com.inklusport.sports.entity.*;
import com.inklusport.sports.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationService {

    private final EventRepository eventRepository;
    private final EventRegistrationRepository registrationRepository;
    private final EventAttendanceRepository attendanceRepository;
    private final WaitlistRepository waitlistRepository;

    private static final String QR_BASE_URL = "https://inklusport.com/checkin/";

    /**
     * Registrar usuario en evento
     * Si hay cupo -> registra directamente
     * Si no hay cupo -> agrega a lista de espera
     */
    @Transactional
    public RegistrationResponse registerToEvent(String userId, RegistrationRequest request) {
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        // Validaciones
        validateEventRegistration(event, userId);

        long confirmedCount = registrationRepository.countConfirmedRegistrations(event.getId());
        long availableCapacity = event.getMaxCapacity() - confirmedCount;

        // Crear registro base
        EventRegistration registration = EventRegistration.builder()
                .userId(userId)
                .event(event)
                .attended(false)
                .build();

        if (availableCapacity > 0) {
            // Registro directo
            registration.setWaitlistPosition(null);
            registration.setQrCode(generateQRCode(userId, event.getId()));
            
            EventRegistration saved = registrationRepository.save(registration);
            log.info("Usuario {} registrado en evento {}", userId, event.getName());
            
            return convertToResponse(saved, event);
        } else {
            // Agregar a lista de espera
            Integer maxPosition = waitlistRepository.findMaxPositionByEventId(event.getId());
            int newPosition = (maxPosition == null) ? 1 : maxPosition + 1;
            
            registration.setWaitlistPosition(newPosition);
            registration.setQrCode(null);
            
            EventRegistration savedRegistration = registrationRepository.save(registration);
            
            // Crear entrada en waitlist
            Waitlist waitlist = Waitlist.builder()
                    .userId(userId)
                    .event(event)
                    .position(newPosition)
                    .status(Waitlist.WaitlistStatus.waiting)
                    .notified(false)
                    .registration(savedRegistration)
                    .build();
            
            waitlistRepository.save(waitlist);
            
            log.info("Usuario {} agregado a lista de espera de {} (posición {})", 
                    userId, event.getName(), newPosition);
            
            return convertToResponse(savedRegistration, event);
        }
    }

    /**
     * Cancelar inscripción
     */
    @Transactional
    public void cancelRegistration(String userId, String eventId) {
        EventRegistration registration = registrationRepository
                .findByUserIdAndEventId(userId, eventId)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));

        Event event = registration.getEvent();
        boolean wasInWaitlist = registration.getWaitlistPosition() != null;

        // Eliminar waitlist si existe
        waitlistRepository.findByUserIdAndEventId(userId, eventId)
                .ifPresent(waitlistRepository::delete);

        // Eliminar registro
        registrationRepository.delete(registration);

        // Si estaba en waitlist, reorganizar posiciones
        if (wasInWaitlist) {
            reorganizeWaitlistPositions(eventId);
        }

        log.info("Usuario {} canceló inscripción en evento {}", userId, event.getName());
    }

    /**
     * Reorganizar posiciones de waitlist
     */
    @Transactional
    public void reorganizeWaitlistPositions(String eventId) {
        List<Waitlist> waitlist = waitlistRepository.findByEventIdOrderByPositionAsc(eventId);
        int newPosition = 1;
        for (Waitlist w : waitlist) {
            w.setPosition(newPosition);
            waitlistRepository.save(w);
            
            // Actualizar también en event_registration
            if (w.getRegistration() != null) {
                w.getRegistration().setWaitlistPosition(newPosition);
                registrationRepository.save(w.getRegistration());
            }
            newPosition++;
        }
    }

    /**
     * Ofrecer cupo al siguiente en lista de espera
     */
    @Transactional
    public WaitlistResponse offerNextFromWaitlist(String eventId, String offeredBy) {
        List<Waitlist> waitlist = waitlistRepository.findByEventIdAndStatusOrderByPositionAsc(
                eventId, Waitlist.WaitlistStatus.waiting);
        
        if (waitlist.isEmpty()) {
            throw new RuntimeException("No hay usuarios en lista de espera");
        }

        Waitlist next = waitlist.get(0);
        next.setStatus(Waitlist.WaitlistStatus.offered);
        next.setNotified(true);
        next.setNotifiedAt(LocalDateTime.now());
        waitlistRepository.save(next);

        log.info("Cupo ofrecido a usuario {} para evento {}", next.getUserId(), eventId);
        
        return convertToWaitlistResponse(next);
    }

    /**
     * Aceptar oferta de lista de espera
     */
    @Transactional
    public RegistrationResponse acceptWaitlistOffer(String userId, String eventId) {
        Waitlist waitlist = waitlistRepository.findByUserIdAndEventId(userId, eventId)
                .orElseThrow(() -> new RuntimeException("No hay oferta pendiente"));

        if (waitlist.getStatus() != Waitlist.WaitlistStatus.offered) {
            throw new RuntimeException("No hay una oferta activa para este usuario");
        }

        Event event = waitlist.getEvent();
        
        // Actualizar waitlist
        waitlist.setStatus(Waitlist.WaitlistStatus.accepted);
        waitlistRepository.save(waitlist);

        // Actualizar registration
        EventRegistration registration = waitlist.getRegistration();
        registration.setWaitlistPosition(null);
        registration.setQrCode(generateQRCode(userId, eventId));
        registrationRepository.save(registration);

        log.info("Usuario {} aceptó oferta y fue registrado en evento {}", userId, event.getName());
        
        return convertToResponse(registration, event);
    }

    /**
     * Rechazar oferta de lista de espera
     */
    @Transactional
    public void rejectWaitlistOffer(String userId, String eventId) {
        Waitlist waitlist = waitlistRepository.findByUserIdAndEventId(userId, eventId)
                .orElseThrow(() -> new RuntimeException("No hay oferta pendiente"));

        waitlist.setStatus(Waitlist.WaitlistStatus.expired);
        waitlistRepository.save(waitlist);

        // Eliminar registration asociada
        if (waitlist.getRegistration() != null) {
            registrationRepository.delete(waitlist.getRegistration());
        }

        // Reorganizar posiciones
        reorganizeWaitlistPositions(eventId);
        
        // Ofrecer al siguiente
        offerNextFromWaitlist(eventId, "system");

        log.info("Usuario {} rechazó oferta para evento {}", userId, eventId);
    }

    /**
     * Check-in mediante QR
     */
    @Transactional
    public void checkInByQr(String qrCode, String verifiedBy) {
        EventRegistration registration = registrationRepository.findByQrCode(qrCode)
                .orElseThrow(() -> new RuntimeException("Código QR inválido"));

        if (registration.getAttended()) {
            throw new RuntimeException("El usuario ya registró su asistencia");
        }

        // Crear attendance record
        EventAttendance attendance = EventAttendance.builder()
                .registration(registration)
                .checkInMethod(EventAttendance.CheckInMethod.qr)
                .verifiedBy(verifiedBy)
                .build();
        attendanceRepository.save(attendance);

        // Marcar como atendido
        registration.setAttended(true);
        registrationRepository.save(registration);

        log.info("Check-in QR para usuario {} en evento {}", 
                registration.getUserId(), registration.getEvent().getName());
    }

    /**
     * Check-in manual (admin/entrenador)
     */
    @Transactional
    public void manualCheckIn(String userId, String eventId, String verifiedBy) {
        EventRegistration registration = registrationRepository
                .findByUserIdAndEventId(userId, eventId)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));

        if (registration.getAttended()) {
            throw new RuntimeException("El usuario ya registró su asistencia");
        }

        EventAttendance attendance = EventAttendance.builder()
                .registration(registration)
                .checkInMethod(EventAttendance.CheckInMethod.manual)
                .verifiedBy(verifiedBy)
                .build();
        attendanceRepository.save(attendance);

        registration.setAttended(true);
        registrationRepository.save(registration);

        log.info("Check-in manual para usuario {} en evento {} por {}", 
                userId, eventId, verifiedBy);
    }

    /**
     * Obtener lista de espera de un evento
     */
    @Transactional(readOnly = true)
    public List<WaitlistResponse> getEventWaitlist(String eventId) {
        return waitlistRepository.findByEventIdOrderByPositionAsc(eventId).stream()
                .map(this::convertToWaitlistResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtener inscripciones de un usuario
     */
    @Transactional(readOnly = true)
    public List<RegistrationResponse> getUserRegistrations(String userId) {
        return registrationRepository.findByUserId(userId).stream()
                .map(reg -> convertToResponse(reg, reg.getEvent()))
                .collect(Collectors.toList());
    }

    /**
     * Obtener todas las inscripciones de un evento
     */
    @Transactional(readOnly = true)
    public List<RegistrationResponse> getEventRegistrations(String eventId) {
        return registrationRepository.findByEventId(eventId).stream()
                .map(reg -> convertToResponse(reg, reg.getEvent()))
                .collect(Collectors.toList());
    }

    /**
     * Verificar si usuario está registrado
     */
    @Transactional(readOnly = true)
    public boolean isUserRegistered(String userId, String eventId) {
        return registrationRepository.existsByUserIdAndEventId(userId, eventId);
    }

    /**
     * Obtener posición en lista de espera
     */
    @Transactional(readOnly = true)
    public Integer getWaitlistPosition(String userId, String eventId) {
        return waitlistRepository.findByUserIdAndEventId(userId, eventId)
                .map(Waitlist::getPosition)
                .orElse(null);
    }

    // Métodos privados auxiliares

    private void validateEventRegistration(Event event, String userId) {
        if (event.getStatus() != Event.EventStatus.active) {
            throw new RuntimeException("El evento no está disponible");
        }
        if (event.getEventDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("No se puede inscribir a un evento que ya pasó");
        }
        if (registrationRepository.existsByUserIdAndEventId(userId, event.getId())) {
            throw new RuntimeException("Ya estás registrado en este evento");
        }
        if (waitlistRepository.existsByUserIdAndEventId(userId, event.getId())) {
            throw new RuntimeException("Ya estás en lista de espera de este evento");
        }
    }

    private String generateQRCode(String userId, String eventId) {
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        return QR_BASE_URL + eventId + "/" + userId + "?token=" + uniqueId;
    }

    private RegistrationResponse convertToResponse(EventRegistration registration, Event event) {
        return RegistrationResponse.builder()
                .id(registration.getId())
                .userId(registration.getUserId())
                .eventId(event.getId())
                .eventName(event.getName())
                .eventDate(event.getEventDate())
                .eventTime(event.getEventTime())
                .location(event.getLocation())
                .registrationDate(registration.getRegistrationDate())
                .attended(registration.getAttended())
                .waitlistPosition(registration.getWaitlistPosition())
                .qrCode(registration.getQrCode())
                .build();
    }

    private WaitlistResponse convertToWaitlistResponse(Waitlist waitlist) {
        return WaitlistResponse.builder()
                .id(waitlist.getId())
                .userId(waitlist.getUserId())
                .eventId(waitlist.getEvent().getId())
                .eventName(waitlist.getEvent().getName())
                .position(waitlist.getPosition())
                .status(waitlist.getStatus().name())
                .requestedAt(waitlist.getRequestedAt())
                .notified(waitlist.getNotified())
                .notifiedAt(waitlist.getNotifiedAt())
                .build();
    }
}