package com.inklusport.users.controller;

import com.inklusport.common.dto.response.ErrorResponse;
import com.inklusport.users.dto.request.AssignRoleRequest;
import com.inklusport.users.dto.response.RoleResponse;
import com.inklusport.users.dto.response.UserProfileResponse;
import com.inklusport.users.service.UserService;
import com.inklusport.users.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;
    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<List<UserProfileResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/active")
    public ResponseEntity<List<UserProfileResponse>> getActiveUsers() {
        return ResponseEntity.ok(userService.getActivateUsers());
    }

    @PostMapping("/{email}/deactivate")
    public ResponseEntity<?> deactivateUser(@PathVariable String email) {
        try {
            userService.desactivateUser(email);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/admin/users/" + email + "/deactivate");
        }
    }

    @PostMapping("/{email}/activate")
    public ResponseEntity<?> activateUser(@PathVariable String email) {
        try {
            userService.activateUser(email);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/admin/users/" + email + "/activate");
        }
    }

    @GetMapping("/roles")
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @PostMapping("/{email}/roles")
    public ResponseEntity<?> assignRole(@PathVariable String email,
                                        @Valid @RequestBody AssignRoleRequest request,
                                        @AuthenticationPrincipal String adminEmail) {
        String targetEmail = decodeEmail(email);
        try {
            return ResponseEntity.ok(roleService.assignRoleToUser(targetEmail, request, adminEmail));
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/admin/users/" + targetEmail + "/roles");
        }
    }

    @GetMapping("/roles-by-email")
    public ResponseEntity<List<String>> getUserRoles(@RequestParam String email) {
        List<String> roles = roleService.getUserRoles(email);
        return ResponseEntity.ok(roles);
    }

    @DeleteMapping("/{email}/roles/{roleId}")
    public ResponseEntity<?> removeRole(@PathVariable String email, @PathVariable Long roleId) {
        try {
            roleService.removeRoleFromUser(email, roleId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/admin/users/" + email + "/roles/" + roleId);
        }
    }

    @GetMapping("/{email}/exists")
    public ResponseEntity<Boolean> userExists(@PathVariable String email) {
        return ResponseEntity.ok(userService.userExists(email));
    }

    private static String decodeEmail(String email) {
        return URLDecoder.decode(email, StandardCharsets.UTF_8);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception e, String path) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message(e.getMessage())
                .path(path)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}