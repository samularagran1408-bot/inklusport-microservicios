package com.inklusport.sports.service;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final WaitlistRepository waitlistRepository;
    private final EventAttendanceRepository attendanceRepository;

    private static final String QR_BASE_URL = "https://inklusport.com/checkin/";

    /**
     * Registrar un usuario en un evento (T77)
     * Si hay cupos disponibles, registra directamente.
     * Si no hay cupos, agrega a lista de espera.
     */
    @Transactional
    public RegistrationResponse registerToEvent(String userId, RegistrationRequest request) {
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con ID: " + request.getEventId()));

        if (event.getStatus() != Event.EventStatus.active) {
            throw new RuntimeException("El evento no está disponible para inscripción");
        }

        if (event.getEventDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("No se puede inscribir a un evento que ya pasó");
        }

        if (registrationRepository.existsByUserIdAndEventId(userId, request.getEventId())) {
            throw new RuntimeException("Ya estás registrado en este evento");
        }

        EventRegistration registration = new EventRegistration();
        registration.setUserId(userId);
        registration.setEvent(event);

        if (event.getAvailableCapacity() > 0) {
            registration.setAttended(false);
            registration.setWaitlistPosition(null);
            registration.setQrCode(generateQRCode(userId, event.getId()));
            
            EventRegistration saved = registrationRepository.save(registration);
            
            int updated = eventRepository.decrementAvailableCapacity(event.getId());
            if (updated == 0) {
                throw new RuntimeException("Error al actualizar el cupo del evento");
            }
            
            log.info("Usuario {} registrado en evento {}", userId, event.getName());
            return convertToResponse(saved);
        } 
        else {
            Integer maxPosition = waitlistRepository.findMaxPositionByEventId(event.getId());
            int newPosition = (maxPosition == null) ? 1 : maxPosition + 1;
            
            registration.setWaitlistPosition(newPosition);
            registration.setAttended(false);
            registration.setQrCode(null); 
            
            EventRegistration saved = registrationRepository.save(registration);
            
            Waitlist waitlist = new Waitlist();
            waitlist.setUserId(userId);
            waitlist.setEvent(event);
            waitlist.setPosition(newPosition);
            waitlist.setStatus(Waitlist.WaitlistStatus.waiting);
            waitlist.setRequestedAt(LocalDateTime.now());
            waitlist.setNotified(false);
            waitlistRepository.save(waitlist);
            
            log.info("Usuario {} agregado a lista de espera de {} (posición {})", 
                    userId, event.getName(), newPosition);
            
            return convertToResponse(saved);
        }
    }

    /**
     * Cancelar inscripción de un usuario en un evento
     */
    @Transactional
    public void cancelRegistration(String userId, String eventId) {
        EventRegistration registration = registrationRepository.findByUserIdAndEventId(userId, eventId)
                .orElseThrow(() -> new RuntimeException("No se encontró inscripción para este evento"));

        Event event = registration.getEvent();
        
        /** Eliminar la inscripción */
        registrationRepository.delete(registration);
        
        /** Si el usuario estaba en lista de espera, eliminarlo también */
        waitlistRepository.findByUserIdAndEventId(userId, eventId)
                .ifPresent(waitlistRepository::delete);
        
        /** Aumentar cupos disponibles */
        eventRepository.incrementAvailableCapacity(eventId);
        
        /** Procesar lista de espera (ofrecer cupo al siguiente) */
        processWaitlist(eventId);
        
        log.info("Usuario {} canceló inscripción en evento {}", userId, event.getName());
    }

    /**
     * Procesar lista de espera - ofrecer cupo al siguiente usuario
     */
    @Transactional
    public void processWaitlist(String eventId) {
        List<Waitlist> waitlist = waitlistRepository.findByEventIdOrderByPositionAsc(eventId);
        
        if (!waitlist.isEmpty()) {
            Waitlist nextInLine = waitlist.get(0);
            
            /** Marcar como ofrecido */
            nextInLine.setStatus(Waitlist.WaitlistStatus.offered);
            nextInLine.setNotified(true);
            nextInLine.setNotifiedAt(LocalDateTime.now());
            waitlistRepository.save(nextInLine);
            
            log.info("Cupo liberado para usuario {} en evento {}", nextInLine.getUserId(), eventId);
            /** Aquí se enviaría notificación al usuario */
        }
    }

    /**
     * Registrar asistencia mediante QR (check-in)
     */
    @Transactional
    public void checkIn(String qrCode, String verifiedBy) {
        /** Buscar la inscripción por código QR */
        EventRegistration registration = registrationRepository.findAll().stream()
                .filter(r -> qrCode.equals(r.getQrCode()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Código QR inválido"));

        if (registration.getAttended()) {
            throw new RuntimeException("El usuario ya registró su asistencia");
        }

        /** Crear registro de asistencia */
        EventAttendance attendance = new EventAttendance();
        attendance.setRegistration(registration);
        attendance.setCheckInMethod(EventAttendance.CheckInMethod.qr);
        attendance.setVerifiedBy(verifiedBy);
        
        attendanceRepository.save(attendance);
        
        /** Marcar como asistió */
        registration.setAttended(true);
        registrationRepository.save(registration);
        
        log.info("Asistencia registrada para usuario {} en evento {}", 
                registration.getUserId(), registration.getEvent().getName());
    }

    /**
     * Registrar asistencia manual (admin/entrenador)
     */
    @Transactional
    public void manualCheckIn(String userId, String eventId, String verifiedBy) {
        EventRegistration registration = registrationRepository.findByUserIdAndEventId(userId, eventId)
                .orElseThrow(() -> new RuntimeException("No se encontró inscripción para este usuario en el evento"));

        if (registration.getAttended()) {
            throw new RuntimeException("El usuario ya registró su asistencia");
        }

        EventAttendance attendance = new EventAttendance();
        attendance.setRegistration(registration);
        attendance.setCheckInMethod(EventAttendance.CheckInMethod.manual);
        attendance.setVerifiedBy(verifiedBy);
        
        attendanceRepository.save(attendance);
        
        registration.setAttended(true);
        registrationRepository.save(registration);
        
        log.info("Asistencia manual registrada para usuario {} en evento {}", userId, eventId);
    }

    /**
     * Obtener todas las inscripciones de un usuario
     */
    @Transactional(readOnly = true)
    public List<RegistrationResponse> getUserRegistrations(String userId) {
        return registrationRepository.findByUserId(userId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Agregar usuario a lista de espera explícitamente
     */
    @Transactional
    public RegistrationResponse addToWaitlist(String userId, WaitlistRequest request) {
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con ID: " + request.getEventId()));

        /** Verificar si el evento está activo */
        if (event.getStatus() != Event.EventStatus.active) {
            throw new RuntimeException("El evento no está disponible");
        }

        /** Verificar si el usuario ya está registrado */
        if (registrationRepository.existsByUserIdAndEventId(userId, request.getEventId())) {
            throw new RuntimeException("Ya estás registrado en este evento");
        }

        /** Verificar si el usuario ya está en lista de espera */
        Integer maxPosition = waitlistRepository.findMaxPositionByEventId(event.getId());
        int newPosition = (maxPosition == null) ? 1 : maxPosition + 1;

        /** Crear inscripción */
        EventRegistration registration = new EventRegistration();
        registration.setUserId(userId);
        registration.setEvent(event);
        registration.setWaitlistPosition(newPosition);
        registration.setAttended(false);

        EventRegistration saved = registrationRepository.save(registration);

        /** Crear entrada en lista de espera */
        Waitlist waitlist = new Waitlist();
        waitlist.setUserId(userId);
        waitlist.setEvent(event);
        waitlist.setPosition(newPosition);
        waitlist.setStatus(Waitlist.WaitlistStatus.waiting);
        waitlist.setNotified(false);

        waitlistRepository.save(waitlist);

        log.info("Usuario {} agregado a lista de espera del evento {} (posición {})", 
                userId, event.getName(), newPosition);

        return convertToResponse(saved);
    }

    /**
     * Obtener todas las inscripciones de un evento
     */
    @Transactional(readOnly = true)
    public List<RegistrationResponse> getEventRegistrations(String eventId) {
        return registrationRepository.findByEventId(eventId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtener lista de espera de un evento
     */
    @Transactional(readOnly = true)
    public List<Waitlist> getEventWaitlist(String eventId) {
        return waitlistRepository.findByEventIdOrderByPositionAsc(eventId);
    }

    /**
     * Verificar si un usuario está registrado en un evento
     */
    @Transactional(readOnly = true)
    public boolean isUserRegistered(String userId, String eventId) {
        return registrationRepository.existsByUserIdAndEventId(userId, eventId);
    }

    /**
     * Obtener conteo de asistentes a un evento
     */
    @Transactional(readOnly = true)
    public long getEventAttendanceCount(String eventId) {
        return registrationRepository.findByEventIdAndAttendedTrue(eventId).size();
    }

    /**
     * Generar código QR único para check-in
     */
    private String generateQRCode(String userId, String eventId) {
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        return QR_BASE_URL + eventId + "/" + userId + "?token=" + uniqueId;
    }

    /**
     * Convertir entidad a Response DTO
     */
    private RegistrationResponse convertToResponse(EventRegistration registration) {
        String eventName = registration.getEvent() != null ? registration.getEvent().getName() : null;
        
        return RegistrationResponse.builder()
                .id(registration.getId())
                .userId(registration.getUserId())
                .eventId(registration.getEvent().getId())
                .eventName(eventName)
                .registrationDate(registration.getRegistrationDate())
                .attended(registration.getAttended())
                .waitlistPosition(registration.getWaitlistPosition())
                .qrCode(registration.getQrCode())
                .build();
    }

    /**
     * Aceptar oferta de lista de espera
     */
    @Transactional
    public void acceptWaitlistOffer(String userId, String eventId) {
        Waitlist waitlist = waitlistRepository.findByUserIdAndEventId(userId, eventId)
                .orElseThrow(() -> new RuntimeException("No se encontró solicitud en lista de espera"));

        if (waitlist.getStatus() != Waitlist.WaitlistStatus.offered) {
            throw new RuntimeException("No hay una oferta pendiente para este usuario");
        }

        /** Actualizar estado de la lista de espera */ 
        waitlist.setStatus(Waitlist.WaitlistStatus.accepted);
        waitlistRepository.save(waitlist);

        /** Actualizar la inscripción (remover posición de lista de espera) */
        EventRegistration registration = registrationRepository.findByUserIdAndEventId(userId, eventId)
                .orElseThrow(() -> new RuntimeException("No se encontró la inscripción"));
        
        registration.setWaitlistPosition(null);
        registrationRepository.save(registration);

        /** Actualizar cupos disponibles del evento */
        eventRepository.decrementAvailableCapacity(eventId);

        log.info("Usuario {} aceptó oferta de lista de espera para evento {}", userId, eventId);
    }

    /**
     * Rechazar oferta de lista de espera
     */
    @Transactional
    public void rejectWaitlistOffer(String userId, String eventId) {
        Waitlist waitlist = waitlistRepository.findByUserIdAndEventId(userId, eventId)
                .orElseThrow(() -> new RuntimeException("No se encontró solicitud en lista de espera"));

        waitlist.setStatus(Waitlist.WaitlistStatus.expired);
        waitlistRepository.save(waitlist);

        /** Procesar siguiente en lista de espera */
        processWaitlist(eventId);

        log.info("Usuario {} rechazó oferta de lista de espera para evento {}", userId, eventId);
    }
}