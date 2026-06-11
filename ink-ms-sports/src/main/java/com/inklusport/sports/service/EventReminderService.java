package com.inklusport.sports.service;

import com.inklusport.sports.client.NotificationServiceClient;
import com.inklusport.sports.dto.NotificationRequest;
import com.inklusport.sports.entity.Event;
import com.inklusport.sports.enums.EventStatus;
import com.inklusport.sports.entity.EventRegistration;
import com.inklusport.sports.repository.EventRegistrationRepository;
import com.inklusport.sports.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventReminderService {

    private final EventRepository eventRepository;
    private final EventRegistrationRepository registrationRepository;
    private final NotificationServiceClient notificationClient;

    /**
     * Ejecuta todos los días a las 8:00 AM
     * Envía recordatorios para eventos que ocurren MAÑANA
     */
    @Scheduled(cron = "0 0 8 * * *")
    @Transactional
    public void sendEventReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        
        log.info("Buscando eventos para mañana: {}", tomorrow);
        
        /**
         * Buscar eventos activos que ocurren mañana
         */
        List<Event> events = eventRepository.findByEventDateAndStatus(tomorrow, EventStatus.active);
        
        if (events.isEmpty()) {
            log.info("No hay eventos programados para mañana");
            return;
        }
        
        log.info("Encontrados {} eventos para mañana", events.size());
        
        for (Event event : events) {
            sendRemindersForEvent(event);
        }
    }
    
    /**
     * Envía recordatorios a todos los inscritos de un evento
     */
    private void sendRemindersForEvent(Event event) {
        List<EventRegistration> registrations = registrationRepository.findByEventId(event.getId());
        
        if (registrations.isEmpty()) {
            log.info("No hay inscritos para el evento: {}", event.getName());
            return;
        }
        
        log.info("Enviando recordatorios para '{}' a {} usuarios", event.getName(), registrations.size());
        
        for (EventRegistration registration : registrations) {
            sendNotification(registration.getUserId(), event);
        }
    }
    
    /**
     * Envía la notificación a un usuario específico
     */
    private void sendNotification(String userId, Event event) {
        try {
            NotificationRequest request = new NotificationRequest();
            request.setUserId(userId);
            request.setType("event_reminder");
            request.setTitle("Recordatorio: Evento mañana");
            request.setBody(String.format(
                "Recuerda que mañana a las %s tienes el evento '%s' en %s. ¡Te esperamos!",
                event.getEventTime().toString(),
                event.getName(),
                event.getLocation() != null ? event.getLocation() : "nuestra sede"
            ));
            request.setEventId(event.getId());
            request.setPriority("high");
            
            notificationClient.createNotification(userId, request);
            log.info("Recordatorio enviado a: {}", userId);
        } catch (Exception e) {
            log.error("Error al enviar recordatorio a {}: {}", userId, e.getMessage());
        }
    }
}