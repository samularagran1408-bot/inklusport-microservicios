package com.inklusport.ia.controller;

import com.inklusport.ia.dto.request.AnalisisBiomecanicoRequest;
import com.inklusport.ia.dto.response.AnalisisBiomecanicoResponse;
import com.inklusport.ia.service.AnalisisBiomecanicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoints del analisis biomecanico.
 * Por ahora la IA es una formula fija (no un modelo externo todavia).
 */
@RestController
@RequestMapping("/api/ia/analisis")
@RequiredArgsConstructor
public class AnalisisBiomecanicoController {

    private final AnalisisBiomecanicoService analisisBiomecanicoService;

    /**
     * Registra un analisis nuevo.
     * El servicio calcula el puntaje y las recomendaciones automaticas.
     */
    @PostMapping
    public ResponseEntity<AnalisisBiomecanicoResponse> registrarAnalisis(
            @Valid @RequestBody AnalisisBiomecanicoRequest request) {
        AnalisisBiomecanicoResponse response = analisisBiomecanicoService.registrarAnalisis(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Trae todo el historial de un usuario (ordenado del mas nuevo al mas viejo).
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<AnalisisBiomecanicoResponse>> obtenerHistorial(
            @PathVariable String usuarioId) {
        return ResponseEntity.ok(analisisBiomecanicoService.consultarHistorialPorUsuario(usuarioId));
    }
}
