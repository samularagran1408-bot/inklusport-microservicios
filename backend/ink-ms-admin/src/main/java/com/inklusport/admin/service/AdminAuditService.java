package com.inklusport.admin.service;

import com.inklusport.admin.client.UserServiceClient;
import com.inklusport.admin.dto.AdminActionRequest;
import com.inklusport.admin.dto.AdminAuditResponse;
import com.inklusport.admin.entity.AdminAuditLog;
import com.inklusport.admin.repository.AdminAuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminAuditService {

    private final AdminAuditLogRepository auditLogRepository;
    private final UserServiceClient userServiceClient;
    /**
     * Registra una acción administrativa en el log de auditoría.  
     * La acción se registra con el ID de usuario del administrador que realizó la acción, 
     * el tipo de entidad que se registró y el ID de la entidad.
     */
    @Transactional
    public void logAction(String adminId, AdminActionRequest request) {
        AdminAuditLog auditLog = AdminAuditLog.builder()
                .id(UUID.randomUUID().toString())
                .adminId(adminId)
                .action(request.getAction())
                .targetType(request.getTargetType())
                .targetId(request.getTargetId())
                .details(request.getDetails() != null ? request.getDetails().toString() : null)
                .ipAddress(request.getIpAddress())
                .build();

        auditLogRepository.save(auditLog);
        log.info("Acción registrada: {} por admin {}", request.getAction(), adminId);
    }

    /**
     * Busca las acciones administrativas realizadas por un administrador
     */
    @Transactional(readOnly = true)
    public Page<AdminAuditResponse> getAdminActions(String adminId, Pageable pageable){
        return auditLogRepository.findByAdminId(adminId, pageable)
                .map(this::convertToResponse);
    }

    /**
     * Busca las acciones administrativas realizadas por un tipo de entidad
     */
    @Transactional(readOnly = true)
    public Page<AdminAuditResponse> getActionsByType(String action, Pageable pageable) {
        return auditLogRepository.findByAction(action, pageable)
                .map(this::convertToResponse);
    }

    /**
     * Busca las acciones administrativas realizadas en un rango de fechas
     */
    @Transactional(readOnly = true)
    public Page<AdminAuditResponse> getActionsByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return auditLogRepository.findByDateRange(startDate, endDate, pageable)
                .map(this::convertToResponse);
    }

    /**
     * Busca las acciones más utilizadas
     */
    @Transactional(readOnly = true)
    public Map<String, Long> getActionStatistics() {
        List<Object[]> results = auditLogRepository.countByAction();
        return results.stream()
                /** separa el resultado en un mapa con la clave como la acción y el valor 
                 * como el número de veces que se ejecutó */
                .collect(Collectors.toMap(
                        r -> (String) r[0],
                        r -> (Long) r[1]
                ));
    }

    /**
     * Convierte un objeto de la base de datos a un objeto de respuesta
     */
    private AdminAuditResponse convertToResponse(AdminAuditLog auditLog) {
        String adminEmail = null;
        String adminName = null;
        try {
            Map<String, Object> user = userServiceClient.getUserById(auditLog.getAdminId());
            if (user != null) {
                adminEmail = (String) user.get("email");
                adminName = (String) user.get("name");
            }
        } catch (Exception e) {
            log.warn("No se pudo obtener información del admin: {}", auditLog.getAdminId());
        }

        return AdminAuditResponse.builder()
                .id(auditLog.getId())
                .adminId(auditLog.getAdminId())
                .adminEmail(adminEmail)
                .adminName(adminName)
                .action(auditLog.getAction())
                .targetType(auditLog.getTargetType())
                .targetId(auditLog.getTargetId())
                .details(auditLog.getDetails())
                .ipAddress(auditLog.getIpAddress())
                .createdAt(auditLog.getCreatedAt())
                .build();
    }
}
