package com.inklusport.admin.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Filtro que intercepta las peticiones HTTP para validar y procesar tokens JWT.
 * Se ejecuta una vez por peticion y extrae la informacion del usuario del token.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Realiza el filtrado de la peticion.
     * Valida el token JWT y establece el contexto de seguridad si el token es valido.
     * @param request Peticion HTTP
     * @param response Respuesta HTTP
     * @param filterChain Cadena de filtros
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = extractTokenFromRequest(request);

            if (token != null && jwtTokenProvider.validateToken(token)) {
                String email = jwtTokenProvider.getEmailFromToken(token);
                List<String> roles = jwtTokenProvider.getRolesFromToken(token);

                // Convertir roles a GrantedAuthority
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList());

                // Crear token de autenticacion
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(email, null, authorities);

                // Establecer el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("Usuario {} autenticado exitosamente", email);
            }
        } catch (Exception e) {
            log.error("Error al procesar el token JWT: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extrae el token JWT del encabezado Authorization de la peticion.
     * El formato esperado es: Authorization: Bearer <token>
     * @param request Peticion HTTP
     * @return Token JWT sin el prefijo "Bearer " o null si no existe
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }
}
