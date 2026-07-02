package com.inklusport.admin.service;

import com.inklusport.admin.dto.SystemConfigRequest;
import com.inklusport.admin.dto.SystemConfigResponse;
import com.inklusport.admin.entity.SystemConfig;
import com.inklusport.admin.exception.ResourceNotFoundException;
import com.inklusport.admin.repository.SystemConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la gestion de parametros de configuracion del sistema.
 * Maneja las configuraciones globales de la plataforma.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SystemConfigService {

    private final SystemConfigRepository systemConfigRepository;

    /**
     * Obtiene todas las configuraciones del sistema.
     * @return Lista de configuraciones
     */
    @Transactional(readOnly = true)
    public List<SystemConfigResponse> getAllConfigs() {
        return systemConfigRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene una configuracion por su clave.
     * @param configKey Clave de configuracion
     * @return Configuracion encontrada
     * @throws ResourceNotFoundException Si no existe
     */
    @Transactional(readOnly = true)
    public SystemConfigResponse getConfigByKey(String configKey) {
        SystemConfig config = systemConfigRepository.findById(configKey)
                .orElseThrow(() -> new ResourceNotFoundException("Configuracion no encontrada: " + configKey));
        return convertToResponse(config);
    }

    /**
     * Crea una nueva configuracion del sistema.
     * @param request Datos de la configuracion
     * @return Configuracion creada
     */
    @Transactional
    public SystemConfigResponse createConfig(SystemConfigRequest request) {
        SystemConfig config = SystemConfig.builder()
                .configKey(request.getConfigKey())
                .configValue(request.getConfigValue())
                .description(request.getDescription())
                .updatedAt(LocalDateTime.now())
                .build();

        SystemConfig saved = systemConfigRepository.save(config);
        log.info("Configuracion del sistema creada: {}", saved.getConfigKey());
        return convertToResponse(saved);
    }

    /**
     * Actualiza una configuracion del sistema existente.
     * @param configKey Clave de la configuracion
     * @param request Nuevos datos
     * @return Configuracion actualizada
     * @throws ResourceNotFoundException Si no existe
     */
    @Transactional
    public SystemConfigResponse updateConfig(String configKey, SystemConfigRequest request) {
        SystemConfig config = systemConfigRepository.findById(configKey)
                .orElseThrow(() -> new ResourceNotFoundException("Configuracion no encontrada: " + configKey));

        config.setConfigValue(request.getConfigValue());
        config.setDescription(request.getDescription());
        config.setUpdatedAt(LocalDateTime.now());

        SystemConfig updated = systemConfigRepository.save(config);
        log.info("Configuracion actualizada: {}", configKey);
        return convertToResponse(updated);
    }

    /**
     * Elimina una configuracion del sistema.
     * @param configKey Clave de la configuracion
     * @throws ResourceNotFoundException Si no existe
     */
    @Transactional
    public void deleteConfig(String configKey) {
        if (!systemConfigRepository.existsById(configKey)) {
            throw new ResourceNotFoundException("Configuracion no encontrada: " + configKey);
        }
        systemConfigRepository.deleteById(configKey);
        log.info("Configuracion eliminada: {}", configKey);
    }

    /**
     * Convierte una entidad SystemConfig a su DTO de respuesta.
     */
    private SystemConfigResponse convertToResponse(SystemConfig config) {
        return SystemConfigResponse.builder()
                .configKey(config.getConfigKey())
                .configValue(config.getConfigValue())
                .description(config.getDescription())
                .updatedAt(config.getUpdatedAt())
                .build();
    }
}
