package com.inklusport.admin.service;

import com.inklusport.admin.dto.ApprovalRequest;
import com.inklusport.admin.dto.PendingApprovalResponse;
import com.inklusport.admin.dto.ReviewRequest;
import com.inklusport.admin.entity.PendingApproval;
import com.inklusport.admin.enums.RequestStatus;
import com.inklusport.admin.repository.PendingApprovalRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApprovalService {

    private final PendingApprovalRepository approvalRepository;
    private final ObjectMapper objectMapper;

    /**
     * Obtiene todas las aprobaciones pendientes.
     */
    @Transactional(readOnly = true)
    public Page<PendingApprovalResponse> getPendingApprovals(Pageable pageable) {
        return approvalRepository.findByStatus(RequestStatus.pending, pageable)
                .map(this::convertToResponse);
    }

    /**
     * Obtiene una aprobacion por su ID.
     */
    @Transactional(readOnly = true)
    public PendingApprovalResponse getApprovalById(String id) {
        PendingApproval approval = approvalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud de aprobacion no encontrada: " + id));
        return convertToResponse(approval);
    }

    /**
     * Crea una nueva solicitud de aprobacion.
     */
    @Transactional
    public PendingApprovalResponse createApproval(ApprovalRequest request) {
        String targetData;
        try {
            targetData = objectMapper.writeValueAsString(request.getTargetData());
        } catch (Exception e) {
            throw new RuntimeException("Error al serializar datos", e);
        }

        PendingApproval approval = PendingApproval.builder()
                .id(UUID.randomUUID().toString())
                .targetType(request.getTargetType())
                .targetData(targetData)
                .requestedBy(request.getRequestedBy())
                .status(RequestStatus.pending)
                .build();

        PendingApproval saved = approvalRepository.save(approval);
        log.info("Solicitud de aprobacion creada para {} por {}", request.getTargetType(), request.getRequestedBy());
        return convertToResponse(saved);
    }

    /**
     * Aprueba una solicitud de aprobacion pendiente.
     */
    @Transactional
    public PendingApprovalResponse approveRequest(String id, ReviewRequest reviewRequest) {
        PendingApproval approval = approvalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud de aprobacion no encontrada: " + id));

        if (approval.getStatus() != RequestStatus.pending) {
            throw new RuntimeException("Esta solicitud ya fue revisada");
        }

        approval.setStatus(RequestStatus.approved);
        approval.setReviewedBy(reviewRequest.getReviewedBy());
        approval.setReviewedAt(LocalDateTime.now());
        approval.setReviewNotes(reviewRequest.getNotes());

        PendingApproval updated = approvalRepository.save(approval);
        log.info("Solicitud aprobada: {} por {}", id, reviewRequest.getReviewedBy());
        
        return convertToResponse(updated);
    }

    /**
     * Rechaza una solicitud de aprobacion pendiente.
     */
    @Transactional
    public PendingApprovalResponse rejectRequest(String id, ReviewRequest reviewRequest) {
        PendingApproval approval = approvalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud de aprobacion no encontrada: " + id));

        if (approval.getStatus() != RequestStatus.pending) {
            throw new RuntimeException("Esta solicitud ya fue revisada");
        }

        approval.setStatus(RequestStatus.rejected);
        approval.setReviewedBy(reviewRequest.getReviewedBy());
        approval.setReviewedAt(LocalDateTime.now());
        approval.setReviewNotes(reviewRequest.getNotes());

        PendingApproval updated = approvalRepository.save(approval);
        log.info("Solicitud rechazada: {} por {}", id, reviewRequest.getReviewedBy());
        
        return convertToResponse(updated);
    }

    /**
     * Obtiene aprobaciones por tipo de entidad.
     */
    @Transactional(readOnly = true)
    public Page<PendingApprovalResponse> getApprovalsByType(String targetType, Pageable pageable) {
        return approvalRepository.findByTargetType(targetType, pageable)
                .map(this::convertToResponse);
    }

    /**
     * Obtiene las solicitudes de aprobacion por estado (metodo original para compatibilidad).
     */
    @Transactional(readOnly = true)
    public Page<PendingApprovalResponse> getApprovalsByStatus(RequestStatus status, Pageable pageable) {
        return approvalRepository.findByStatus(status, pageable)
                .map(this::convertToResponse);
    }

    /**
     * Revisa una solicitud de aprobacion (metodo original para compatibilidad).
     */
    @Transactional
    public PendingApprovalResponse reviewApproval(String approvalId, ReviewRequest request, String reviewedBy) {
        PendingApproval approval = approvalRepository.findById(approvalId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (approval.getStatus() != RequestStatus.pending) {
            throw new RuntimeException("Esta solicitud ya fue revisada");
        }

        RequestStatus newStatus = RequestStatus.valueOf(request.getDecision().toUpperCase());
        
        approval.setStatus(newStatus);
        approval.setReviewedBy(reviewedBy);
        approval.setReviewedAt(LocalDateTime.now());
        approval.setReviewNotes(request.getNotes());

        PendingApproval updated = approvalRepository.save(approval);
        log.info("Solicitud {} {} por {}", approvalId, newStatus, reviewedBy);
        
        return convertToResponse(updated);
    }

    /**
     * Crea una solicitud de aprobacion (metodo original para compatibilidad).
     */
    @Transactional
    public PendingApprovalResponse submitForApproval(String requestedBy, ApprovalRequest request) {
        String targetData;
        try {
            targetData = objectMapper.writeValueAsString(request.getTargetData());
        } catch (Exception e) {
            throw new RuntimeException("Error al serializar datos", e);
        }

        PendingApproval approval = PendingApproval.builder()
                .id(UUID.randomUUID().toString())
                .targetType(request.getTargetType())
                .targetData(targetData)
                .requestedBy(requestedBy)
                .status(RequestStatus.pending)
                .build();

        PendingApproval saved = approvalRepository.save(approval);
        log.info("Solicitud de aprobacion creada para {} por {}", request.getTargetType(), requestedBy);
        return convertToResponse(saved);
    }

    /**
     * Convierte un objeto de la base de datos a un objeto de respuesta
     */
    private PendingApprovalResponse convertToResponse(PendingApproval approval) {
        Object targetData = null;
        try {
            targetData = objectMapper.readValue(approval.getTargetData(), Object.class);
        } catch (Exception e) {
            log.warn("Error al deserializar targetData: {}", e.getMessage());
        }

        return PendingApprovalResponse.builder()
                .id(approval.getId())
                .targetType(approval.getTargetType())
                .targetData(targetData)
                .requestedBy(approval.getRequestedBy())
                .requestedAt(approval.getRequestedAt())
                .status(approval.getStatus().name())
                .reviewedBy(approval.getReviewedBy())
                .reviewedAt(approval.getReviewedAt())
                .reviewNotes(approval.getReviewNotes())
                .build();
    }
}