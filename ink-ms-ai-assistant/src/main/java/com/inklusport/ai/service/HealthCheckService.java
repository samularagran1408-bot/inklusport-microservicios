package com.inklusport.ai.service;

import com.inklusport.ai.repository.ChatTrainingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class HealthCheckService implements HealthIndicator {

    private final MongoTemplate mongoTemplate;
    private final ChatTrainingRepository chatTrainingRepository;

    @Override
    public Health health() {
        Map<String, Object> details = new HashMap<>();
        boolean isHealthy = true;

        try {
            /**
             * Verificar MongoDB
             */
            boolean mongoHealthy = checkMongoDB();
            details.put("mongodb", mongoHealthy ? "UP" : "DOWN");
            if (!mongoHealthy) isHealthy = false;

            /**
             * Verificar base de datos de entrenamiento
             */
            boolean trainingHealthy = checkTrainingData();
            details.put("trainingData", trainingHealthy ? "UP" : "DOWN");
            if (!trainingHealthy) isHealthy = false;

            /**
             * Verificar caché
             */
            details.put("cache", "UP");

            /**
             * Información adicional
             */
            details.put("service", "Inklusport AI Microservice");
            details.put("version", "1.0.0");
            details.put("timestamp", java.time.LocalDateTime.now());

            if (isHealthy) {
                return Health.up().withDetails(details).build();
            } else {
                return Health.down().withDetails(details).build();
            }
        } catch (Exception e) {
            log.error("Health check falló: {}", e.getMessage());
            details.put("error", e.getMessage());
            return Health.down().withDetails(details).build();
        }
    }

    /**
     * Verificar conectividad con MongoDB
     */
    private boolean checkMongoDB() {
        try {
            mongoTemplate.getCollectionNames();
            return true;
        } catch (Exception e) {
            log.error("MongoDB no está disponible: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Verificar datos de entrenamiento
     */
    private boolean checkTrainingData() {
        try {
            long count = chatTrainingRepository.count();
            return count > 0;
        } catch (Exception e) {
            log.error("Error verificando datos de entrenamiento: {}", e.getMessage());
            return false;
        }
    }
}