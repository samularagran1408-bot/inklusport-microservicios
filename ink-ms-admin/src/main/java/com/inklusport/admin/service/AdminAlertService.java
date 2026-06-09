package com.inklusport.admin.service;

import com.inklusport.admin.dto.AdminAlertResponse;
import com.inklusport.admin.dto.AlertRequest;
import com.inklusport.admin.entity.AdminAlert;
import com.inklusport.admin.exception.ResourceNotFoundException;
import com.inklusport.admin.repository.AdminAlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Servicio para la gestion de alertas administrativas.
 * Maneja la creacion, resolucion y consulta de alertas del sistema.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminAlertService {

    private final AdminAlertRepository adminAlertRepository;

    /**
     * Obtiene todas las alertas con paginacion.
     * @param pageable Parametros de paginacion
     * @return Pagina con alertas
     */
    @Transactional(readOnly = true)
    public Page<AdminAlertResponse> getAllAlerts(Pageable pageable) {
        return adminAlertRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    /**
     * Obtiene una alerta por su ID.
     * @param id ID de la alerta
     * @return Alerta encontrada
     * @throws ResourceNotFoundException Si no existe
     */
    @Transactional(readOnly = true)
    public AdminAlertResponse getAlertById(String id) {
        AdminAlert alert = adminAlertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta no encontrada con ID: " + id));
        return convertToResponse(alert);
    }

    /**
     * Crea una nueva alerta administrativa.
     * @param request Datos de la alerta
     * @return Alerta creada
     */
    @Transactional
    public AdminAlertResponse createAlert(AlertRequest request) {
        AdminAlert alert = AdminAlert.builder()
                .id(UUID.randomUUID().toString())
                .type(request.getType())
                .severity(request.getSeverity())
                .title(request.getTitle())
                .description(request.getDescription())
                .targetId(request.getTargetId())
                .targetType(request.getTargetType())
                .resolved(false)
                .build();

        AdminAlert saved = adminAlertRepository.save(alert);
        log.info("Alerta creada: {} - {}", saved.getType(), saved.getTitle());
        return convertToResponse(saved);
    }

    /**
     * Marca una alerta como resuelta.
     * @param id ID de la alerta
     * @param adminId ID del administrador que resuelve
     * @return Alerta actualizada
     * @throws ResourceNotFoundException Si no existe
     */
    @Transactional
    public AdminAlertResponse resolveAlert(String id, String adminId) {
        AdminAlert alert = adminAlertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta no encontrada con ID: " + id));

        alert.setResolved(true);
        alert.setResolvedBy(adminId);
        alert.setResolvedAt(LocalDateTime.now());

        AdminAlert updated = adminAlertRepository.save(alert);
        log.info("Alerta resuelta: {} por {}", id, adminId);
        return convertToResponse(updated);
    }

    /**
     * Obtiene todas las alertas no resueltas.
     * @param pageable Parametros de paginacion
     * @return Pagina con alertas no resueltas
     */
    @Transactional(readOnly = true)
    public Page<AdminAlertResponse> getUnresolvedAlerts(Pageable pageable) {
        // Ordenar por severidad descendente (critical primero)
        Pageable pageableWithSort = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "severity")
        );
        
        return adminAlertRepository.findByResolvedFalse(pageableWithSort)
                .map(this::convertToResponse);
    }

    /**
     * Obtiene alertas por nivel de gravedad.
     * @param severity Nivel de gravedad
     * @param pageable Parametros de paginacion
     * @return Pagina con alertas del nivel especificado
     */
    @Transactional(readOnly = true)
    public Page<AdminAlertResponse> getAlertsBySeverity(String severity, Pageable pageable) {
        return adminAlertRepository.findBySeverity(severity, pageable)
                .map(this::convertToResponse);
    }

    /**
     * Convierte una entidad AdminAlert a su DTO de respuesta.
     */
    private AdminAlertResponse convertToResponse(AdminAlert alert) {
        return AdminAlertResponse.builder()
                .id(alert.getId())
                .type(alert.getType())
                .severity(alert.getSeverity())
                .title(alert.getTitle())
                .description(alert.getDescription())
                .targetId(alert.getTargetId())
                .targetType(alert.getTargetType())
                .resolved(alert.getResolved())
                .resolvedBy(alert.getResolvedBy())
                .resolvedAt(alert.getResolvedAt())
                .createdAt(alert.getCreatedAt())
                .build();
    }
}
