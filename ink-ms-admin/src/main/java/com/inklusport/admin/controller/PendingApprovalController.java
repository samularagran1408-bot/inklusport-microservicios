package com.inklusport.admin.controller;

import com.inklusport.admin.dto.ApprovalRequest;
import com.inklusport.admin.dto.PendingApprovalResponse;
import com.inklusport.admin.dto.ReviewRequest;
import com.inklusport.admin.service.ApprovalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para el flujo de aprobaciones administrativas.
 * Gestiona las solicitudes de aprobacion que requieren revision de administradores.
 */
@RestController
@RequestMapping("/api/v1/admin/approvals")
@RequiredArgsConstructor
public class PendingApprovalController {

    private final ApprovalService approvalService;

    /**
     * Obtiene la lista de todas las aprobaciones pendientes.
     * @param pageable Parametros de paginacion
     * @return Pagina con aprobaciones pendientes
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<PendingApprovalResponse>> getPendingApprovals(Pageable pageable) {
        Page<PendingApprovalResponse> approvals = approvalService.getPendingApprovals(pageable);
        return ResponseEntity.ok(approvals);
    }

    /**
     * Obtiene una aprobacion especifica por su ID.
     * @param id ID de la aprobacion
     * @return Aprobacion encontrada
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PendingApprovalResponse> getApprovalById(@PathVariable String id) {
        PendingApprovalResponse approval = approvalService.getApprovalById(id);
        return ResponseEntity.ok(approval);
    }

    /**
     * Crea una nueva solicitud de aprobacion.
     * @param request Datos de la solicitud de aprobacion
     * @return Aprobacion creada
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PendingApprovalResponse> createApproval(@Valid @RequestBody ApprovalRequest request) {
        PendingApprovalResponse approval = approvalService.createApproval(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(approval);
    }

    /**
     * Aprueba una solicitud de aprobacion pendiente.
     * @param id ID de la solicitud a aprobar
     * @param reviewRequest Datos de la revision (notas opcionales)
     * @return Aprobacion actualizada
     */
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PendingApprovalResponse> approveRequest(
            @PathVariable String id,
            @Valid @RequestBody ReviewRequest reviewRequest) {
        PendingApprovalResponse approval = approvalService.approveRequest(id, reviewRequest);
        return ResponseEntity.ok(approval);
    }

    /**
     * Rechaza una solicitud de aprobacion pendiente.
     * @param id ID de la solicitud a rechazar
     * @param reviewRequest Datos de la revision (notas requeridas para explicar el rechazo)
     * @return Aprobacion actualizada
     */
    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PendingApprovalResponse> rejectRequest(
            @PathVariable String id,
            @Valid @RequestBody ReviewRequest reviewRequest) {
        PendingApprovalResponse approval = approvalService.rejectRequest(id, reviewRequest);
        return ResponseEntity.ok(approval);
    }

    /**
     * Obtiene las aprobaciones de un tipo especifico de entidad.
     * @param targetType Tipo de entidad (event, sport, disability, etc)
     * @param pageable Parametros de paginacion
     * @return Pagina con aprobaciones del tipo especificado
     */
    @GetMapping("/by-type/{targetType}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<PendingApprovalResponse>> getApprovalsByType(
            @PathVariable String targetType,
            Pageable pageable) {
        Page<PendingApprovalResponse> approvals = approvalService.getApprovalsByType(targetType, pageable);
        return ResponseEntity.ok(approvals);
    }
}
