package com.inklusport.admin.controller;

import com.inklusport.admin.dto.AssignRoleRequest;
import com.inklusport.admin.dto.RoleRequest;
import com.inklusport.admin.dto.RoleResponse;
import com.inklusport.admin.service.AdminRoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para la gestion de roles administrativos.
 * Proporciona endpoints para crear, leer, actualizar y eliminar roles.
 * Tambien permite asignar roles a administradores.
 */
@RestController
@RequestMapping("/api/v1/admin/roles")
@RequiredArgsConstructor
public class AdminRoleController {

    private final AdminRoleService adminRoleService;

    /**
     * Obtiene la lista de todos los roles disponibles.
     * @return Lista de roles
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        List<RoleResponse> roles = adminRoleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    /**
     * Obtiene un rol especifico por su ID.
     * @param id ID del rol a obtener
     * @return Rol encontrado
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleResponse> getRoleById(@PathVariable Integer id) {
        RoleResponse role = adminRoleService.getRoleById(id);
        return ResponseEntity.ok(role);
    }

    /**
     * Crea un nuevo rol administrativo.
     * @param request Datos del rol a crear
     * @return Rol creado
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody RoleRequest request) {
        RoleResponse role = adminRoleService.createRole(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(role);
    }

    /**
     * Actualiza un rol existente.
     * @param id ID del rol a actualizar
     * @param request Nuevos datos del rol
     * @return Rol actualizado
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleResponse> updateRole(
            @PathVariable Integer id,
            @Valid @RequestBody RoleRequest request) {
        RoleResponse role = adminRoleService.updateRole(id, request);
        return ResponseEntity.ok(role);
    }

    /**
     * Elimina un rol existente.
     * @param id ID del rol a eliminar
     * @return Confirmacion de eliminacion
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRole(@PathVariable Integer id) {
        adminRoleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Asigna un rol a un administrador.
     * @param request Datos de asignacion del rol
     * @return Confirmacion de asignacion
     */
    @PostMapping("/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> assignRoleToAdmin(@Valid @RequestBody AssignRoleRequest request) {
        adminRoleService.assignRoleToAdmin(request);
        return ResponseEntity.ok("Rol asignado exitosamente");
    }

    /**
     * Remueve un rol de un administrador.
     * @param adminId ID del administrador
     * @param roleId ID del rol a remover
     * @return Confirmacion de remocion
     */
    @DeleteMapping("/remove/{adminId}/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> removeRoleFromAdmin(
            @PathVariable String adminId,
            @PathVariable Integer roleId) {
        adminRoleService.removeRoleFromAdmin(adminId, roleId);
        return ResponseEntity.ok("Rol removido exitosamente");
    }
}
