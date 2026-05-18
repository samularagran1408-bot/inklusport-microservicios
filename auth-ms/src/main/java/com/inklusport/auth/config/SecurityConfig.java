package com.inklusport.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  /**
   * Configuración de seguridad
   * @param http
   * @return
   * @throws Exception
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
              /** 
               * Endpoints PÚBLICOS (NO requieren token)
               */ 
              .requestMatchers("/api/auth/register").permitAll()
              .requestMatchers("/api/auth/login").permitAll()
              .requestMatchers("/api/auth/forgot-password").permitAll()
              .requestMatchers("/api/auth/reset-password").permitAll()
              .requestMatchers("/actuator/health").permitAll()
              
              /** 
               * Endpoints PROTEGIDOS (requieren token) 
               */ 
              .requestMatchers("/api/auth/logout").authenticated()
              .requestMatchers("/api/auth/validate").authenticated()
            
              .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
  }

  /**
   * Bean para codificar contraseñas con BCrypt
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}