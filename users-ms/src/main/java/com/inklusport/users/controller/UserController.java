package com.inklusport.users.controller;

import com.inklusport.users.dto.request.UpdateProfileRequest;
import com.inklusport.users.dto.response.UserProfileResponse;
import com.inklusport.users.dto.response.UserActivityResponse;
import com.inklusport.users.dto.response.ErrorResponse;
import com.inklusport.users.service.UserService;
import com.inklusport.users.service.UserActivityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserActivityService userActivityService;

    @PostMapping("/perfil")
    public ResponseEntity<?> createMyProfile(@AuthenticationPrincipal String email,
                                              @Valid @RequestBody UpdateProfileRequest request,
                                              HttpServletRequest httpRequest) {
        try {
            UserProfileResponse response = userService.createUserProfile(email, request.getFullName());
            
            // Actualizar el resto de campos si vienen en la petición
            if (request.getPhone() != null || 
                request.getProfilePicture() != null || request.getBio() != null) {
                response = userService.updateUserProfile(email, request);
            }
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/users/perfil");
        }
    }

    @GetMapping("/perfil")
    public ResponseEntity<?> getMyProfile(@AuthenticationPrincipal String email) {
        try {
            UserProfileResponse response = userService.getUserProfileByEmail(email);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/users/perfil");
        }
    }

    @PutMapping("/perfil")
    public ResponseEntity<?> updateMyProfile(@AuthenticationPrincipal String email,
                                              @Valid @RequestBody UpdateProfileRequest request,
                                              HttpServletRequest httpRequest) {
        try {
            UserProfileResponse response = userService.updateUserProfile(email, request);

            userActivityService.logActivity(email, "UPDATE_PROFILE", 
                "Perfil actualizado", httpRequest.getRemoteAddr());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/users/perfil");
        }
    }

    @GetMapping("/perfil/activities")
    public ResponseEntity<?> getMyActivities(@AuthenticationPrincipal String email) {
        try {
            List<UserActivityResponse> response = userActivityService.getUserActivities(email);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/users/perfil/activities");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        try {
            UserProfileResponse response = userService.getUserProfileById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/users/" + id);
        }
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
