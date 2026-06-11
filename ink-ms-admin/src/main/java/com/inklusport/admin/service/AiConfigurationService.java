package com.inklusport.admin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inklusport.admin.dto.AiConfigRequest;
import com.inklusport.admin.dto.AiConfigResponse;
import com.inklusport.admin.entity.AiConfiguration;
import com.inklusport.admin.exception.ResourceNotFoundException;
import com.inklusport.admin.exception.DuplicateResourceException;
import com.inklusport.admin.repository.AiConfigurationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la gestion de configuraciones de funcionalidades de IA.
 * Maneja el ciclo de vida de las configuraciones de modelos de IA.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AiConfigurationService {

    private final AiConfigurationRepository aiConfigurationRepository;
    private final ObjectMapper objectMapper;

    /**
     * Obtiene todas las configuraciones de IA.
     * @return Lista de configuraciones
     */
    @Transactional(readOnly = true)
    public List<AiConfigResponse> getAllConfigurations() {
        return aiConfigurationRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene una configuracion de IA por su ID.
     * @param id ID de la configuracion
     * @return Configuracion encontrada
     * @throws ResourceNotFoundException Si no existe
     */
    @Transactional(readOnly = true)
    public AiConfigResponse getConfigurationById(Integer id) {
        AiConfiguration config = aiConfigurationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Configuracion de IA no encontrada con ID: " + id));
        return convertToResponse(config);
    }

    /**
     * Obtiene una configuracion de IA por el nombre de la funcionalidad.
     * @param featureName Nombre de la funcionalidad
     * @return Configuracion encontrada
     * @throws ResourceNotFoundException Si no existe
     */
    @Transactional(readOnly = true)
    public AiConfigResponse getConfigurationByFeatureName(String featureName) {
        AiConfiguration config = aiConfigurationRepository.findByFeatureName(featureName)
                .orElseThrow(() -> new ResourceNotFoundException("Configuracion de IA no encontrada para: " + featureName));
        return convertToResponse(config);
    }

    /**
     * Crea una nueva configuracion de IA.
     * @param request Datos de la configuracion
     * @return Configuracion creada
     * @throws DuplicateResourceException Si la funcionalidad ya existe
     */
    @Transactional
    public AiConfigResponse createConfiguration(AiConfigRequest request) {
        if (aiConfigurationRepository.existsByFeatureName(request.getFeatureName())) {
            throw new DuplicateResourceException("Ya existe una configuracion para: " + request.getFeatureName());
        }

        AiConfiguration config = AiConfiguration.builder()
                .featureName(request.getFeatureName())
                .isEnabled(request.getIsEnabled() != null ? request.getIsEnabled() : true)
                .modelVersion(request.getModelVersion())
                .confidenceThreshold(request.getConfidenceThreshold())
                .parameters(toJson(request.getParameters()))
                .updatedBy(request.getUpdatedBy())
                .build();

        AiConfiguration saved = aiConfigurationRepository.save(config);
        log.info("Configuracion de IA creada para funcionalidad: {}", saved.getFeatureName());
        return convertToResponse(saved);
    }

    /**
     * Actualiza una configuracion de IA existente.
     * @param id ID de la configuracion
     * @param request Nuevos datos
     * @return Configuracion actualizada
     * @throws ResourceNotFoundException Si no existe
     */
    @Transactional
    public AiConfigResponse updateConfiguration(Integer id, AiConfigRequest request) {
        AiConfiguration config = aiConfigurationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Configuracion de IA no encontrada con ID: " + id));

        config.setModelVersion(request.getModelVersion());
        config.setConfidenceThreshold(request.getConfidenceThreshold());
        config.setParameters(toJson(request.getParameters()));
        config.setUpdatedBy(request.getUpdatedBy());
        config.setUpdatedAt(LocalDateTime.now());

        AiConfiguration updated = aiConfigurationRepository.save(config);
        log.info("Configuracion de IA actualizada: {}", updated.getFeatureName());
        return convertToResponse(updated);
    }

    /**
     * Habilita una funcionalidad de IA.
     * @param id ID de la configuracion
     * @return Configuracion actualizada
     * @throws ResourceNotFoundException Si no existe
     */
    @Transactional
    public AiConfigResponse enableFeature(Integer id) {
        AiConfiguration config = aiConfigurationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Configuracion de IA no encontrada con ID: " + id));

        config.setIsEnabled(true);
        config.setUpdatedAt(LocalDateTime.now());

        AiConfiguration updated = aiConfigurationRepository.save(config);
        log.info("Funcionalidad de IA habilitada: {}", updated.getFeatureName());
        return convertToResponse(updated);
    }

    /**
     * Deshabilita una funcionalidad de IA.
     * @param id ID de la configuracion
     * @return Configuracion actualizada
     * @throws ResourceNotFoundException Si no existe
     */
    @Transactional
    public AiConfigResponse disableFeature(Integer id) {
        AiConfiguration config = aiConfigurationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Configuracion de IA no encontrada con ID: " + id));

        config.setIsEnabled(false);
        config.setUpdatedAt(LocalDateTime.now());

        AiConfiguration updated = aiConfigurationRepository.save(config);
        log.info("Funcionalidad de IA deshabilitada: {}", updated.getFeatureName());
        return convertToResponse(updated);
    }

    /**
     * Convierte una entidad AiConfiguration a su DTO de respuesta.
     */
    private AiConfigResponse convertToResponse(AiConfiguration config) {
        return AiConfigResponse.builder()
                .id(config.getId())
                .featureName(config.getFeatureName())
                .isEnabled(config.getIsEnabled())
                .modelVersion(config.getModelVersion())
                .confidenceThreshold(config.getConfidenceThreshold())
                .parameters(config.getParameters())
                .updatedBy(config.getUpdatedBy())
                .updatedAt(config.getUpdatedAt())
                .build();
    }

    private String toJson(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("No se pudo serializar el valor JSON", e);
        }
    }
}
