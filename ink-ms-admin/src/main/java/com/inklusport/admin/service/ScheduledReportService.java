package com.inklusport.admin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inklusport.admin.dto.ReportRequest;
import com.inklusport.admin.dto.ReportResponse;
import com.inklusport.admin.entity.ScheduledReport;
import com.inklusport.admin.enums.ReportType;
import com.inklusport.admin.exception.ResourceNotFoundException;
import com.inklusport.admin.repository.ScheduledReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Servicio para la gestion de reportes programados.
 * Maneja la creacion, actualizacion y consulta de reportes automaticos.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduledReportService {

    private final ScheduledReportRepository scheduledReportRepository;
    private final ObjectMapper objectMapper;

    /**
     * Obtiene todos los reportes programados.
     * @param pageable Parametros de paginacion
     * @return Pagina con reportes
     */
    @Transactional(readOnly = true)
    public Page<ReportResponse> getAllReports(Pageable pageable) {
        return scheduledReportRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    /**
     * Obtiene un reporte por su ID.
     * @param id ID del reporte
     * @return Reporte encontrado
     * @throws ResourceNotFoundException Si no existe
     */
    @Transactional(readOnly = true)
    public ReportResponse getReportById(String id) {
        ScheduledReport report = scheduledReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado con ID: " + id));
        return convertToResponse(report);
    }

    /**
     * Crea un nuevo reporte programado.
     * @param request Datos del reporte
     * @return Reporte creado
     */
    @Transactional
    public ReportResponse createReport(ReportRequest request) {
        ScheduledReport report = ScheduledReport.builder()
                .id(UUID.randomUUID().toString())
                .name(request.getName())
                .type(ReportType.valueOf(request.getType()))
                .scheduleCron(request.getScheduleCron())
                .parameters(toJson(request.getParameters()))
                .recipients(toJson(request.getRecipients()))
                .createdAt(LocalDateTime.now())
                .build();

        ScheduledReport saved = scheduledReportRepository.save(report);
        log.info("Reporte programado creado: {}", saved.getName());
        return convertToResponse(saved);
    }

    /**
     * Actualiza un reporte programado existente.
     * @param id ID del reporte
     * @param request Nuevos datos
     * @return Reporte actualizado
     * @throws ResourceNotFoundException Si no existe
     */
    @Transactional
    public ReportResponse updateReport(String id, ReportRequest request) {
        ScheduledReport report = scheduledReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado con ID: " + id));

        report.setName(request.getName());
        report.setType(ReportType.valueOf(request.getType()));
        report.setScheduleCron(request.getScheduleCron());
        report.setParameters(toJson(request.getParameters()));
        report.setRecipients(toJson(request.getRecipients()));

        ScheduledReport updated = scheduledReportRepository.save(report);
        log.info("Reporte actualizado: {}", updated.getName());
        return convertToResponse(updated);
    }

    /**
     * Elimina un reporte programado.
     * @param id ID del reporte
     * @throws ResourceNotFoundException Si no existe
     */
    @Transactional
    public void deleteReport(String id) {
        if (!scheduledReportRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reporte no encontrado con ID: " + id);
        }
        scheduledReportRepository.deleteById(id);
        log.info("Reporte eliminado: {}", id);
    }

    /**
     * Obtiene reportes por tipo especifico.
     * @param reportType Tipo de reporte
     * @param pageable Parametros de paginacion
     * @return Pagina con reportes del tipo especificado
     */
    @Transactional(readOnly = true)
    public Page<ReportResponse> getReportsByType(String reportType, Pageable pageable) {
        return scheduledReportRepository.findByType(ReportType.valueOf(reportType), pageable)
                .map(this::convertToResponse);
    }

    /**
     * Convierte una entidad ScheduledReport a su DTO de respuesta.
     */
    private ReportResponse convertToResponse(ScheduledReport report) {
        return ReportResponse.builder()
                .id(report.getId())
                .name(report.getName())
                .type(report.getType() != null ? report.getType().name() : null)
                .scheduleCron(report.getScheduleCron())
                .parameters(report.getParameters())
                .recipients(fromJsonList(report.getRecipients()))
                .createdAt(report.getCreatedAt())
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

    private List<String> fromJsonList(String json) {
        if (json == null) {
            return null;
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("No se pudo deserializar la lista JSON", e);
        }
    }
}
