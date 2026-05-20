package com.inklusport.sports.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configuración de seguridad para Sports & Events MS
     * 
     * NOTA: Este microservicio NO genera tokens, solo los valida.
     * La autenticación se delega al API Gateway o al filtro JWT.
     * Por ahora, permitimos todas las peticiones para desarrollo.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos (catálogos visibles sin login)
                        .requestMatchers("/api/sports/**").permitAll()
                        .requestMatchers("/api/disabilities/**").permitAll()
                        .requestMatchers("/api/events/**").permitAll()
                        .requestMatchers("/api/calendar/**").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        // Cualquier otra petición - permitida por ahora (desarrollo)
                        .anyRequest().permitAll()
                )
                .build();
    }
}