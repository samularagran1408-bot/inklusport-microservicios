package com.inklusport.sports.service;

import com.inklusport.sports.client.UserServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserIdentityService {

    private final UserServiceClient userServiceClient;

    public String requireUserId(String email) {
        try {
            Map<String, String> profile = userServiceClient.getUserIdByEmail(email);
            String userId = profile.get("userId");
            if (userId == null || userId.isBlank()) {
                throw new RuntimeException("No se pudo resolver el ID del usuario");
            }
            return userId;
        } catch (Exception e) {
            throw new RuntimeException(
                    "Perfil no encontrado. Crea tu perfil en users-ms (POST /api/users/perfil) antes de inscribirte a eventos.",
                    e);
        }
    }
}
