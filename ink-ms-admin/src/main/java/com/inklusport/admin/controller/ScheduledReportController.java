package com.inklusport.admin.controller;

import com.inklusport.admin.dto.ReportRequest;
import com.inklusport.admin.dto.ReportResponse;
import com.inklusport.admin.service.ScheduledReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para la gestion de reportes programados.
 * Permite crear, configurar y listar reportes que se generan automaticamente.
 */
@RestController
@RequestMapping("/api/v1/admin/reports")
@RequiredArgsConstructor
public class ScheduledReportController {

    private final ScheduledReportService scheduledReportService;

    /**
     * Obtiene la lista de todos los reportes programados.
     * @param pageable Parametros de paginacion
     * @return Pagina con reportes programados
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ReportResponse>> getAllReports(Pageable pageable) {
        Page<ReportResponse> reports = scheduledReportService.getAllReports(pageable);
        return ResponseEntity.ok(reports);
    }

    /**
     * Obtiene un reporte especifico por su ID.
     * @param id ID del reporte
     * @return Reporte encontrado
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReportResponse> getReportById(@PathVariable String id) {
        ReportResponse report = scheduledReportService.getReportById(id);
        return ResponseEntity.ok(report);
    }

    /**
     * Crea un nuevo reporte programado.
     * @param request Datos del reporte a crear
     * @return Reporte creado
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReportResponse> createReport(@Valid @RequestBody ReportRequest request) {
        ReportResponse report = scheduledReportService.createReport(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(report);
    }

    /**
     * Actualiza un reporte programado existente.
     * @param id ID del reporte a actualizar
     * @param request Nuevos datos del reporte
     * @return Reporte actualizado
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReportResponse> updateReport(
            @PathVariable String id,
            @Valid @RequestBody ReportRequest request) {
        ReportResponse report = scheduledReportService.updateReport(id, request);
        return ResponseEntity.ok(report);
    }

    /**
     * Elimina un reporte programado.
     * @param id ID del reporte a eliminar
     * @return Confirmacion de eliminacion
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteReport(@PathVariable String id) {
        scheduledReportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene reportes por tipo especifico.
     * @param reportType Tipo de reporte (participants, attendance, disabilities, etc)
     * @param pageable Parametros de paginacion
     * @return Pagina con reportes del tipo especificado
     */
    @GetMapping("/by-type/{reportType}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ReportResponse>> getReportsByType(
            @PathVariable String reportType,
            Pageable pageable) {
        Page<ReportResponse> reports = scheduledReportService.getReportsByType(reportType, pageable);
        return ResponseEntity.ok(reports);
    }
}
