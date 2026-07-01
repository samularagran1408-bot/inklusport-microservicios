package com.inklusport.admin.controller;

import com.inklusport.admin.dto.AlertResponse;
import com.inklusport.admin.dto.AlertRequest;
import com.inklusport.admin.service.AdminAlertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para la gestion de alertas administrativas.
 * Permite crear, consultar y resolver alertas de sistema.
 */
@RestController
@RequestMapping("/api/admin/alerts")
@RequiredArgsConstructor
public class AdminAlertController {

    private final AdminAlertService adminAlertService;

    /**
     * Obtiene todas las alertas sin resolver.
     * @param pageable Parametros de paginacion
     * @return Pagina con alertas activas
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AlertResponse>> getAllAlerts(Pageable pageable) {
        Page<AlertResponse> alerts = adminAlertService.getAllAlerts(pageable);
        return ResponseEntity.ok(alerts);
    }

    /**
     * Obtiene una alerta especifica por su ID.
     * @param id ID de la alerta
     * @return Alerta encontrada
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AlertResponse> getAlertById(@PathVariable String id) {
        AlertResponse alert = adminAlertService.getAlertById(id);
        return ResponseEntity.ok(alert);
    }

    /**
     * Crea una nueva alerta administrativa.
     * @param request Datos de la alerta
     * @return Alerta creada
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AlertResponse> createAlert(@Valid @RequestBody AlertRequest request) {
        AlertResponse alert = adminAlertService.createAlert(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(alert);
    }

    /**
     * Marca una alerta como resuelta.
     * @param id ID de la alerta a resolver
     * @param adminId ID del administrador que resuelve la alerta
     * @return Alerta actualizada
     */
    @PutMapping("/{id}/resolve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AlertResponse> resolveAlert(
            @PathVariable String id,
            @RequestParam String adminId) {
        AlertResponse alert = adminAlertService.resolveAlert(id, adminId);
        return ResponseEntity.ok(alert);
    }

    /**
     * Obtiene todas las alertas no resueltas (ordenadas por gravedad).
     * @param pageable Parametros de paginacion
     * @return Pagina con alertas activas
     */
    @GetMapping("/unresolved")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AlertResponse>> getUnresolvedAlerts(Pageable pageable) {
        Page<AlertResponse> alerts = adminAlertService.getUnresolvedAlerts(pageable);
        return ResponseEntity.ok(alerts);
    }

    /**
     * Obtiene alertas por nivel de gravedad.
     * @param severity Nivel de gravedad (low, medium, high, critical)
     * @param pageable Parametros de paginacion
     * @return Pagina con alertas del nivel especificado
     */
    @GetMapping("/by-severity/{severity}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AlertResponse>> getAlertsBySeverity(
            @PathVariable String severity,
            Pageable pageable) {
        Page<AlertResponse> alerts = adminAlertService.getAlertsBySeverity(severity, pageable);
        return ResponseEntity.ok(alerts);
    }
}
