package com.inklusport.admin.service;

import com.inklusport.admin.dto.UserBlockRequest;
import com.inklusport.admin.dto.UserBlockResponse;
import com.inklusport.admin.enums.BlockType;
import com.inklusport.admin.entity.UserBlock;
import com.inklusport.admin.exception.ResourceNotFoundException;
import com.inklusport.admin.repository.UserBlockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Servicio para la gestion de bloqueos de usuarios.
 * Maneja la creacion, resolucion y consulta de bloqueos de usuarios.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserBlockService {

    private final UserBlockRepository userBlockRepository;

    /**
     * Obtiene todos los usuarios bloqueados.
     * @param pageable Parametros de paginacion
     * @return Pagina con usuarios bloqueados
     */
    @Transactional(readOnly = true)
    public Page<UserBlockResponse> getBlockedUsers(Pageable pageable) {
        return userBlockRepository.findByIsActiveTrue(pageable)
                .map(this::convertToResponse);
    }

    /**
     * Obtiene un bloqueo por su ID.
     * @param id ID del bloqueo
     * @return Bloqueo encontrado
     * @throws ResourceNotFoundException Si no existe
     */
    @Transactional(readOnly = true)
    public UserBlockResponse getBlockById(String id) {
        UserBlock block = userBlockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bloqueo no encontrado con ID: " + id));
        return convertToResponse(block);
    }

    /**
     * Obtiene el bloqueo activo de un usuario.
     * @param userId ID del usuario
     * @return Bloqueo del usuario
     * @throws ResourceNotFoundException Si el usuario no esta bloqueado
     */
    @Transactional(readOnly = true)
    public UserBlockResponse getBlockByUserId(String userId) {
        UserBlock block = userBlockRepository.findByUserIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException("El usuario no esta bloqueado: " + userId));
        return convertToResponse(block);
    }

    /**
     * Bloquea un usuario.
     * @param request Datos del bloqueo
     * @return Bloqueo creado
     */
    @Transactional
    public UserBlockResponse blockUser(UserBlockRequest request) {
        // Desactivar bloqueos previos si existen
        userBlockRepository.findByUserIdAndIsActiveTrue(request.getUserId())
                .ifPresent(existingBlock -> {
                    existingBlock.setIsActive(false);
                    userBlockRepository.save(existingBlock);
                });

        UserBlock block = UserBlock.builder()
                .id(UUID.randomUUID().toString())
                .userId(request.getUserId())
                .blockType(BlockType.valueOf(request.getBlockType()))
                .reason(request.getReason())
                .blockedBy(request.getBlockedBy())
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        UserBlock saved = userBlockRepository.save(block);
        log.info("Usuario bloqueado: {} por {}", request.getUserId(), request.getBlockedBy());
        return convertToResponse(saved);
    }

    /**
     * Desbloquea un usuario removiendo su bloqueo activo.
     * @param id ID del bloqueo a remover
     * @throws ResourceNotFoundException Si el bloqueo no existe
     */
    @Transactional
    public void unblockUser(String id) {
        UserBlock block = userBlockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bloqueo no encontrado con ID: " + id));

        block.setIsActive(false);
        block.setUnblockedAt(LocalDateTime.now());

        userBlockRepository.save(block);
        log.info("Usuario desbloqueado: {}", block.getUserId());
    }

    /**
     * Obtiene bloqueos por tipo especifico.
     * @param blockType Tipo de bloqueo
     * @param pageable Parametros de paginacion
     * @return Pagina con bloqueos del tipo especificado
     */
    @Transactional(readOnly = true)
    public Page<UserBlockResponse> getBlocksByType(String blockType, Pageable pageable) {
        return userBlockRepository.findByBlockTypeAndIsActiveTrue(BlockType.valueOf(blockType), pageable)
                .map(this::convertToResponse);
    }

    /**
     * Convierte una entidad UserBlock a su DTO de respuesta.
     */
    private UserBlockResponse convertToResponse(UserBlock block) {
        return UserBlockResponse.builder()
                .id(block.getId())
                .userId(block.getUserId())
                .blockType(block.getBlockType() != null ? block.getBlockType().name() : null)
                .reason(block.getReason())
                .blockedBy(block.getBlockedBy())
                .isActive(block.getIsActive())
                .createdAt(block.getCreatedAt())
                .unblockedAt(block.getUnblockedAt())
                .build();
    }
}
