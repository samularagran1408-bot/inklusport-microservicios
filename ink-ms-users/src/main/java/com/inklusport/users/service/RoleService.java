package com.inklusport.users.service;

import com.inklusport.users.dto.request.AssignRoleRequest;
import com.inklusport.users.dto.response.RoleResponse;
import com.inklusport.users.entity.Role;
import com.inklusport.users.entity.User;
import com.inklusport.users.entity.UserRole;
import com.inklusport.users.entity.UserRoleId;
import com.inklusport.users.repository.RoleRepository;
import com.inklusport.users.repository.UserRepository;
import com.inklusport.users.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Transactional(readOnly = true)
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void assignRoleToUser(String userEmail, AssignRoleRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        if (userRoleRepository.existsByUserIdAndRoleId(user.getId(), request.getRoleId())) {
            throw new RuntimeException("El usuario ya tiene este rol asignado");
        }

        UserRoleId id = new UserRoleId(user.getId(), request.getRoleId());
        UserRole userRole = new UserRole();
        userRole.setId(id);
        userRole.setUser(user);
        userRole.setRole(role);

        userRoleRepository.save(userRole);
        log.info("Rol {} asignado a usuario {}", role.getName(), userEmail);
    }

    @Transactional
    public void removeRoleFromUser(String userEmail, Long roleId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!userRoleRepository.existsByUserIdAndRoleId(user.getId(), roleId)) {
            throw new RuntimeException("El usuario no tiene este rol asignado");
        }

        userRoleRepository.deleteByUserIdAndRoleId(user.getId(), roleId);
        log.info("Rol {} removido de usuario {}", roleId, userEmail);
    }

    @Transactional(readOnly = true)
    public List<String> getUserRoles(String email) {
        return userRepository.findByEmail(email)
                .map(user -> userRoleRepository.findRoleNamesByUserId(user.getId()))
                .orElse(List.of());
    }

    private RoleResponse convertToResponse(Role role) {
        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .build();
    }
}