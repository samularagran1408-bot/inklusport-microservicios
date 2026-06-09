package com.inklusport.admin.service;

import com.inklusport.admin.dto.PermissionRequest;
import com.inklusport.admin.dto.PermissionResponse;
import com.inklusport.admin.entity.Permission;
import com.inklusport.admin.entity.RolePermission;
import com.inklusport.admin.entity.RolePermissionId;
import com.inklusport.admin.enums.PermissionAction;
import com.inklusport.admin.repository.PermissionRepository;
import com.inklusport.admin.repository.RolePermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    /**
     * Busca todos los permisos
     */
    @Transactional(readOnly = true)
    public List<PermissionResponse> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Busca un permiso por ID
     */
    @Transactional(readOnly = true)
    public PermissionResponse getPermissionById(Integer id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado con ID: " + id));
        return convertToResponse(permission);
    }

    /**
     * Busca permisos por recurso
     */
    @Transactional(readOnly = true)
    public List<PermissionResponse> getPermissionsByResource(String resource) {
        return permissionRepository.findByResource(resource).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Crea un permiso
     */
    @Transactional
    public PermissionResponse createPermission(PermissionRequest request) {
        if (permissionRepository.findByName(request.getName()).isPresent()) {
            throw new RuntimeException("Ya existe un permiso con el nombre: " + request.getName());
        }

        Permission permission = Permission.builder()
                .name(request.getName())
                .resource(request.getResource())
                .action(PermissionAction.valueOf(request.getAction()))
                .description(request.getDescription())
                .build();

        Permission saved = permissionRepository.save(permission);
        log.info("Permiso creado: {}", saved.getName());
        return convertToResponse(saved);
    }

    /**
     * Actualiza un permiso existente
     */
    @Transactional
    public PermissionResponse updatePermission(Integer id, PermissionRequest request) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado con ID: " + id));

        permission.setName(request.getName());
        permission.setResource(request.getResource());
        permission.setAction(PermissionAction.valueOf(request.getAction()));
        permission.setDescription(request.getDescription());

        Permission updated = permissionRepository.save(permission);
        log.info("Permiso actualizado: {}", updated.getName());
        return convertToResponse(updated);
    }

    /**
     * Obtiene los permisos asignados a un rol
     */
    @Transactional(readOnly = true)
    public List<PermissionResponse> getPermissionsByRole(Integer roleId) {
        return rolePermissionRepository.findPermissionsByRoleId(roleId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Asigna un permiso a un rol
     */
    @Transactional
    public void assignPermissionToRole(Integer roleId, Integer permissionId) {
        RolePermissionId id = new RolePermissionId(roleId, permissionId);
        if (!rolePermissionRepository.existsById(id)) {
            RolePermission rolePermission = RolePermission.builder()
                    .id(id)
                    .build();
            rolePermissionRepository.save(rolePermission);
            log.info("Permiso {} asignado al rol {}", permissionId, roleId);
        }
    }

    /**
     * Remueve un permiso de un rol
     */
    @Transactional
    public void removePermissionFromRole(Integer roleId, Integer permissionId) {
        RolePermissionId id = new RolePermissionId(roleId, permissionId);
        rolePermissionRepository.deleteById(id);
        log.info("Permiso {} removido del rol {}", permissionId, roleId);
    }

    /**
     * Busca los permisos asignados a un rol (retorna solo nombres)
     */
    @Transactional(readOnly = true)
    public List<String> getRolePermissions(Integer roleId) {
        return rolePermissionRepository.findPermissionNamesByRoleId(roleId);
    }

    /**
     * Asigna un permiso a un rol (version antigua para compatibilidad)
     */
    @Transactional
    public void assignPermissionToRole(Integer roleId, Integer permissionId, String assignedBy) {
        RolePermissionId id = new RolePermissionId(roleId, permissionId);
        if (!rolePermissionRepository.existsById(id)) {
            RolePermission rolePermission = RolePermission.builder()
                    .id(id)
                    .build();
            rolePermissionRepository.save(rolePermission);
            log.info("Permiso {} asignado al rol {} por {}", permissionId, roleId, assignedBy);
        }
    }

    /**
     * Convierte un objeto de la base de datos a un objeto de respuesta
     */
    private PermissionResponse convertToResponse(Permission permission) {
        return PermissionResponse.builder()
                .id(permission.getId())
                .name(permission.getName())
                .resource(permission.getResource())
                .action(permission.getAction().name())
                .description(permission.getDescription())
                .build();
    }
}