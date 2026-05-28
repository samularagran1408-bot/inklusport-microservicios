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

    /**
     * inyección de repositorios
     */
    private final PendingApprovalRepository approvalRepository;
    private final ObjectMapper objectMapper;

    /**
     * Crea una solicitud de aprobación
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
        log.info("Solicitud de aprobación creada para {} por {}", request.getTargetType(), requestedBy);
        return convertToResponse(saved);
    }

    /**
     * Busca las solicitudes de aprobación pendientes
     */
    @Transactional(readOnly = true)
    public Page<PendingApprovalResponse> getPendingApprovals(Pageable pageable) {
        return approvalRepository.findByStatus(RequestStatus.pending, pageable)
                .map(this::convertToResponse);
    }

    /**
     * Busca las solicitudes de aprobación por estado
     */
    @Transactional(readOnly = true)
    public Page<PendingApprovalResponse> getApprovalsByStatus(RequestStatus status, Pageable pageable) {
        return approvalRepository.findByStatus(status, pageable)
                .map(this::convertToResponse);
    }

    /**
     * Revisa una solicitud de aprobación
     */
    @Transactional
    public PendingApprovalResponse reviewApproval(String approvalId, ReviewRequest request, String reviewedBy) {
        PendingApproval approval = approvalRepository.findById(approvalId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (approval.getStatus() != RequestStatus.pending) {
            throw new RuntimeException("Esta solicitud ya fue revisada");
        }

        RequestStatus newStatus = RequestStatus.valueOf(request.getDecision().toUpperCase());
        
        approvalRepository.updateStatus(
                approvalId,
                newStatus,
                reviewedBy,
                request.getNotes()
        );

        // Recargar la entidad actualizada
        approval = approvalRepository.findById(approvalId).get();
        log.info("Solicitud {} {} por {}", approvalId, newStatus, reviewedBy);
        
        return convertToResponse(approval);
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