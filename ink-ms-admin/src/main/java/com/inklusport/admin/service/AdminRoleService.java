package com.inklusport.admin.service;

import com.inklusport.admin.dto.AssignRoleRequest;
import com.inklusport.admin.dto.RoleRequest;
import com.inklusport.admin.dto.RoleResponse;
import com.inklusport.admin.entity.AdminRole;
import com.inklusport.admin.entity.AdminUserRole;
import com.inklusport.admin.entity.AdminUserRoleId;
import com.inklusport.admin.repository.AdminRoleRepository;
import com.inklusport.admin.repository.AdminUserRoleRepository;
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
public class AdminRoleService {

    /**
     * se inyectan los repositorios
     */
    private final AdminRoleRepository roleRepository;
    private final AdminUserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;

    /**
     * Busca todas las roles
     */
    @Transactional(readOnly = true)
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Busca un rol por ID
     */
    @Transactional(readOnly = true)
    public RoleResponse getRoleById(Integer id) {
        AdminRole role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));
        return convertToResponse(role);
    }

    /**
     * Crea un rol
     */
    @Transactional
    public RoleResponse createRole(RoleRequest request) {
        if (roleRepository.existsByName(request.getName())) {
            throw new RuntimeException("Ya existe un rol con el nombre: " + request.getName());
        }

        AdminRole role = AdminRole.builder()
                .name(request.getName().toUpperCase())
                .description(request.getDescription())
                .build();

        AdminRole saved = roleRepository.save(role);
        log.info("Rol creado: {}", saved.getName());
        return convertToResponse(saved);
    }

    /**
     * Actualiza un rol
     */
    @Transactional
    public RoleResponse updateRole(Integer id, RoleRequest request) {
        AdminRole role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));

        role.setName(request.getName().toUpperCase());
        role.setDescription(request.getDescription());

        AdminRole updated = roleRepository.save(role);
        log.info("Rol actualizado: {}", updated.getName());
        return convertToResponse(updated);
    }

    /**
     * Elimina un rol
     */
    @Transactional
    public void deleteRole(Integer id) {
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Rol no encontrado con ID: " + id);
        }
        roleRepository.deleteById(id);
        log.info("Rol eliminado con ID: {}", id);
    }

    /**
     * Asigna un rol a un administrador
     */
    @Transactional
    public void assignRoleToAdmin(String adminId, Integer roleId, String assignedBy) {
        AdminRole role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + roleId));

        AdminUserRoleId id = new AdminUserRoleId(adminId, roleId);
        
        if (userRoleRepository.existsById(id)) {
            throw new RuntimeException("El admin ya tiene este rol asignado");
        }

        AdminUserRole userRole = AdminUserRole.builder()
                .id(id)
                .role(role)
                .assignedBy(assignedBy)
                .build();

        userRoleRepository.save(userRole);
        log.info("Rol {} asignado al admin {} por {}", role.getName(), adminId, assignedBy);
    }

    /**
     * Remueve un rol de un administrador
     */
    @Transactional
    public void removeRoleFromAdmin(String adminId, Integer roleId) {
        AdminUserRoleId id = new AdminUserRoleId(adminId, roleId);
        
        if (!userRoleRepository.existsById(id)) {
            throw new RuntimeException("El admin no tiene este rol asignado");
        }

        userRoleRepository.deleteById(id);
        log.info("Rol {} removido del admin {}", roleId, adminId);
    }

    @Transactional(readOnly = true)
    public List<String> getAdminRoles(String adminId) {
        return userRoleRepository.findRoleNamesByAdminId(adminId);
    }

    private RoleResponse convertToResponse(AdminRole role) {
        List<String> permissions = rolePermissionRepository.findPermissionNamesByRoleId(role.getId());
        Long adminCount = userRoleRepository.findByIdRoleId(role.getId()).stream().count();

        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .createdAt(role.getCreatedAt())
                .permissions(permissions)
                .adminCount(adminCount)
                .build();
    }
}