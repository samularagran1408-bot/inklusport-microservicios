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
     * Ejecuta cada minuto y envía recordatorios para eventos que comienzan dentro de 2 horas.
     */
    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void sendEventReminders() {
        sendEventReminders(LocalDateTime.now());
    }

    @Transactional
    void sendEventReminders(LocalDateTime now) {
        LocalDate targetDate = now.toLocalDate();
        LocalTime targetTime = now.toLocalTime().plusHours(2);

        log.info("Buscando eventos que comienzan dentro de 2 horas a partir de {}", now);

        List<Event> events = eventRepository.findByStatus(EventStatus.active).stream()
                .filter(event -> event.getEventDate().equals(targetDate))
                .filter(event -> !event.getEventTime().isBefore(now.toLocalTime()))
                .filter(event -> !event.getEventTime().isAfter(targetTime))
                .toList();

        if (events.isEmpty()) {
            log.info("No hay eventos programados para las próximas 2 horas");
            return;
        }

        log.info("Encontrados {} eventos para las próximas 2 horas", events.size());

        for (Event event : events) {
            sendRemindersForEvent(event);
        }
    }
    
    /**
     * Envia recordatorios para un evento
     * @param event Evento
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
     * Envia notificación para un usuario
     * @param userId ID del usuario
     * @param event Evento
     */
    private void sendNotification(String userId, Event event) {
        try {
            NotificationRequest request = new NotificationRequest();
            request.setUserId(userId);
            request.setType("event_reminder");
            request.setTitle("Recordatorio: Tu evento empieza pronto");
            request.setBody(String.format(
                "Recuerda que tu evento '%s' comienza a las %s en %s. Falta poco para que empiece. ¡Te esperamos!",
                event.getName(),
                event.getEventTime().toString(),
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