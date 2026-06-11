package com.inklusport.admin.controller;

import com.inklusport.admin.dto.UserBlockRequest;
import com.inklusport.admin.dto.UserBlockResponse;
import com.inklusport.admin.service.UserBlockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para la gestion de bloqueos de usuarios.
 * Permite bloquear y desbloquear usuarios de la plataforma.
 */
@RestController
@RequestMapping("/api/admin/user-blocks")
@RequiredArgsConstructor
public class UserBlockController {

    private final UserBlockService userBlockService;

    /**
     * Obtiene la lista de usuarios bloqueados.
     * @param pageable Parametros de paginacion
     * @return Pagina con usuarios bloqueados
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserBlockResponse>> getBlockedUsers(Pageable pageable) {
        Page<UserBlockResponse> blockedUsers = userBlockService.getBlockedUsers(pageable);
        return ResponseEntity.ok(blockedUsers);
    }

    /**
     * Obtiene un bloqueo especifico por su ID.
     * @param id ID del bloqueo
     * @return Bloqueo encontrado
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserBlockResponse> getBlockById(@PathVariable String id) {
        UserBlockResponse block = userBlockService.getBlockById(id);
        return ResponseEntity.ok(block);
    }

    /**
     * Obtiene el bloqueo de un usuario especifico.
     * @param userId ID del usuario
     * @return Bloqueo del usuario
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserBlockResponse> getBlockByUserId(@PathVariable String userId) {
        UserBlockResponse block = userBlockService.getBlockByUserId(userId);
        return ResponseEntity.ok(block);
    }

    /**
     * Bloquea un usuario.
     * @param request Datos del bloqueo (motivo, tipo, etc)
     * @return Bloqueo creado
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserBlockResponse> blockUser(@Valid @RequestBody UserBlockRequest request) {
        UserBlockResponse block = userBlockService.blockUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(block);
    }

    /**
     * Desbloquea un usuario.
     * @param id ID del bloqueo a remover
     * @return Confirmacion de desbloqueo
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> unblockUser(@PathVariable String id) {
        userBlockService.unblockUser(id);
        return ResponseEntity.ok("Usuario desbloqueado exitosamente");
    }

    /**
     * Obtiene bloqueos por tipo especifico.
     * @param blockType Tipo de bloqueo
     * @param pageable Parametros de paginacion
     * @return Pagina con bloqueos del tipo especificado
     */
    @GetMapping("/by-type/{blockType}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserBlockResponse>> getBlocksByType(
            @PathVariable String blockType,
            Pageable pageable) {
        Page<UserBlockResponse> blocks = userBlockService.getBlocksByType(blockType, pageable);
        return ResponseEntity.ok(blocks);
    }
}
