package com.inklusport.auth.service;

import com.inklusport.auth.client.UserServiceClient;
import com.inklusport.auth.config.EmailAlreadyRegisteredException;
import com.inklusport.auth.dto.AuthResponse;
import com.inklusport.auth.dto.LoginRequest;
import com.inklusport.auth.dto.RegisterRequest;
import com.inklusport.auth.entity.AuthUser;
import com.inklusport.auth.entity.LoginAttempt;
import com.inklusport.auth.repository.AuthUserRepository;
import com.inklusport.auth.repository.LoginAttemptRepository;
import com.inklusport.auth.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

  private final AuthUserRepository authUserRepository;
  private final LoginAttemptRepository loginAttemptRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final UserServiceClient userServiceClient;

  @Value("${security.rate-limit.max-attempts:5}")
  private int maxAttempts;

  @Value("${security.rate-limit.block-duration-minutes:15}")
  private int blockDurationMinutes;

  @Transactional
  public AuthResponse register(RegisterRequest request, String ipAddress) {
    if (authUserRepository.existsByEmail(request.getEmail())) {
      throw new EmailAlreadyRegisteredException(request.getEmail());
    }

    AuthUser user = new AuthUser();
    user.setEmail(request.getEmail());
    user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
    user.setIsActive(true);

    authUserRepository.save(user);

    logLoginAttempt(request.getEmail(), ipAddress, true);

    String token = jwtTokenProvider.generateToken(user.getEmail());

    log.info("Nuevo usuario registrado: {}", user.getEmail());

    return AuthResponse.builder()
            .token(token)
            .tipo("Bearer")
            .id(null)
            .nombre(request.getNombre())
            .email(user.getEmail())
            .build();
  }

  @Transactional
  public AuthResponse login(LoginRequest request, String ipAddress) {
    checkBruteForceBlock(request.getEmail(), ipAddress);

    AuthUser user = authUserRepository.findByEmail(request.getEmail()).orElse(null);
    boolean success = user != null
            && passwordEncoder.matches(request.getPassword(), user.getPasswordHash())
            && Boolean.TRUE.equals(user.getIsActive());

    logLoginAttempt(request.getEmail(), ipAddress, success);

    if (!success) {
      throw new RuntimeException("Credenciales inválidas");
    }

    log.info("Email del usuario: {}", request.getEmail());

    

    log.info("Usuario autenticado: {}", user.getEmail());

    List<String> roles;
    try {
        roles = userServiceClient.getUserRoles(request.getEmail());
    } catch (Exception e) {
        log.warn("No se pudieron obtener roles desde Users MS: {}", e.getMessage());
        roles = List.of();
    }
    log.info("Roles obtenidos desde Users MS: {}", roles);

    if (roles == null || roles.isEmpty()) {
        log.warn("Sin roles en Users MS, asignando USUARIO por defecto");
        roles = List.of("USUARIO");
    }

    authUserRepository.updateLastLogin(request.getEmail(), LocalDateTime.now());
    String token = jwtTokenProvider.generateToken(user.getEmail(), roles);

    return AuthResponse.builder()
            .token(token)
            .tipo("Bearer")
            .id(null)
            .nombre(null)
            .email(user.getEmail())
            .build();
  }

  private void logLoginAttempt(String email, String ipAddress, boolean successful) {
    LoginAttempt attempt = new LoginAttempt();
    attempt.setEmail(email);
    attempt.setIpAddress(ipAddress);
    attempt.setSuccessful(successful);

    loginAttemptRepository.save(attempt);

    if (!successful) {
      log.warn("Intento de login fallido - Email: {}, IP: {}", email, ipAddress);
    }
  }

  private void checkBruteForceBlock(String email, String ipAddress) {
    LocalDateTime since = LocalDateTime.now().minusMinutes(blockDurationMinutes);

    long emailFailures = loginAttemptRepository.countRecentFailuresByEmail(email, since);
    long ipFailures = loginAttemptRepository.countRecentFailuresByIp(ipAddress, since);

    if (emailFailures >= maxAttempts) {
      throw new RuntimeException("Demasiados intentos fallidos. Cuenta temporalmente bloqueada por " + blockDurationMinutes + " minutos.");
    }

    if (ipFailures >= maxAttempts) {
      throw new RuntimeException("Demasiados intentos fallidos desde esta IP. Intente más tarde.");
    }
  }
}
