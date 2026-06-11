package com.inklusport.admin.controller;

import com.inklusport.admin.dto.AdminAuditResponse;
import com.inklusport.admin.service.AdminAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * Controlador para la auditoria de acciones administrativas.
 * Proporciona endpoints para consultar el registro de auditoría.
 */
@RestController
@RequestMapping("/api/admin/audit")
@RequiredArgsConstructor
public class AdminAuditController {

    private final AdminAuditService adminAuditService;

    /**
     * Obtiene las acciones realizadas por un administrador especifico.
     * @param adminId ID del administrador
     * @param pageable Parametros de paginacion
     * @return Pagina con las acciones del administrador
     */
    @GetMapping("/admin/{adminId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AdminAuditResponse>> getAdminActions(
            @PathVariable String adminId,
            Pageable pageable) {
        Page<AdminAuditResponse> actions = adminAuditService.getAdminActions(adminId, pageable);
        return ResponseEntity.ok(actions);
    }

    /**
     * Obtiene las acciones por tipo de accion realizada.
     * @param action Tipo de accion
     * @param pageable Parametros de paginacion
     * @return Pagina con las acciones del tipo especificado
     */
    @GetMapping("/by-action/{action}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AdminAuditResponse>> getActionsByType(
            @PathVariable String action,
            Pageable pageable) {
        Page<AdminAuditResponse> actions = adminAuditService.getActionsByType(action, pageable);
        return ResponseEntity.ok(actions);
    }

    /**
     * Obtiene las acciones en un rango de fechas especificado.
     * @param startDate Fecha de inicio (formato: yyyy-MM-ddTHH:mm:ss)
     * @param endDate Fecha de fin (formato: yyyy-MM-ddTHH:mm:ss)
     * @param pageable Parametros de paginacion
     * @return Pagina con las acciones en el rango de fechas
     */
    @GetMapping("/by-date-range")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AdminAuditResponse>> getActionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable) {
        Page<AdminAuditResponse> actions = adminAuditService.getActionsByDateRange(startDate, endDate, pageable);
        return ResponseEntity.ok(actions);
    }

    /**
     * Obtiene estadisticas de acciones (conteos por tipo de accion).
     * @return Mapa con estadisticas de acciones
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getActionStatistics() {
        var statistics = adminAuditService.getActionStatistics();
        return ResponseEntity.ok(statistics);
    }
}
