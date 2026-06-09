package com.inklusport.admin.controller;

import com.inklusport.admin.dto.PermissionRequest;
import com.inklusport.admin.dto.PermissionResponse;
import com.inklusport.admin.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para la gestion de permisos administrativos.
 * Permite crear, consultar y asignar permisos a roles administrativos.
 */
@RestController
@RequestMapping("/api/v1/admin/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    /**
     * Obtiene la lista de todos los permisos disponibles.
     * @return Lista de permisos
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PermissionResponse>> getAllPermissions() {
        List<PermissionResponse> permissions = permissionService.getAllPermissions();
        return ResponseEntity.ok(permissions);
    }

    /**
     * Obtiene un permiso especifico por su ID.
     * @param id ID del permiso
     * @return Permiso encontrado
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PermissionResponse> getPermissionById(@PathVariable Integer id) {
        PermissionResponse permission = permissionService.getPermissionById(id);
        return ResponseEntity.ok(permission);
    }

    /**
     * Crea un nuevo permiso.
     * @param request Datos del permiso a crear
     * @return Permiso creado
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PermissionResponse> createPermission(@Valid @RequestBody PermissionRequest request) {
        PermissionResponse permission = permissionService.createPermission(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(permission);
    }

    /**
     * Actualiza un permiso existente.
     * @param id ID del permiso a actualizar
     * @param request Nuevos datos del permiso
     * @return Permiso actualizado
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PermissionResponse> updatePermission(
            @PathVariable Integer id,
            @Valid @RequestBody PermissionRequest request) {
        PermissionResponse permission = permissionService.updatePermission(id, request);
        return ResponseEntity.ok(permission);
    }

    /**
     * Obtiene los permisos asignados a un rol especifico.
     * @param roleId ID del rol
     * @return Lista de permisos del rol
     */
    @GetMapping("/role/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PermissionResponse>> getPermissionsByRole(@PathVariable Integer roleId) {
        List<PermissionResponse> permissions = permissionService.getPermissionsByRole(roleId);
        return ResponseEntity.ok(permissions);
    }

    /**
     * Asigna un permiso a un rol.
     * @param roleId ID del rol
     * @param permissionId ID del permiso
     * @return Confirmacion de asignacion
     */
    @PostMapping("/role/{roleId}/permission/{permissionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> assignPermissionToRole(
            @PathVariable Integer roleId,
            @PathVariable Integer permissionId) {
        permissionService.assignPermissionToRole(roleId, permissionId);
        return ResponseEntity.ok("Permiso asignado exitosamente");
    }

    /**
     * Remueve un permiso de un rol.
     * @param roleId ID del rol
     * @param permissionId ID del permiso
     * @return Confirmacion de remocion
     */
    @DeleteMapping("/role/{roleId}/permission/{permissionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> removePermissionFromRole(
            @PathVariable Integer roleId,
            @PathVariable Integer permissionId) {
        permissionService.removePermissionFromRole(roleId, permissionId);
        return ResponseEntity.ok("Permiso removido exitosamente");
    }
}
