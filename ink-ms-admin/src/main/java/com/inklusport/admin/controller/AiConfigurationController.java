package com.inklusport.admin.controller;

import com.inklusport.admin.dto.AiConfigRequest;
import com.inklusport.admin.dto.AiConfigResponse;
import com.inklusport.admin.service.AiConfigurationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para la configuracion de funcionalidades de IA.
 * Permite habilitar/deshabilitar caracteristicas de IA y ajustar parametros.
 */
@RestController
@RequestMapping("/api/admin/ai-config")
@RequiredArgsConstructor
public class AiConfigurationController {

    private final AiConfigurationService aiConfigurationService;

    /**
     * Obtiene la lista de todas las configuraciones de IA.
     * @return Lista de configuraciones
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AiConfigResponse>> getAllConfigurations() {
        List<AiConfigResponse> configurations = aiConfigurationService.getAllConfigurations();
        return ResponseEntity.ok(configurations);
    }

    /**
     * Obtiene una configuracion de IA por su ID.
     * @param id ID de la configuracion
     * @return Configuracion encontrada
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AiConfigResponse> getConfigurationById(@PathVariable Integer id) {
        AiConfigResponse config = aiConfigurationService.getConfigurationById(id);
        return ResponseEntity.ok(config);
    }

    /**
     * Obtiene una configuracion de IA por el nombre de la funcionalidad.
     * @param featureName Nombre de la funcionalidad IA
     * @return Configuracion encontrada
     */
    @GetMapping("/by-feature/{featureName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AiConfigResponse> getConfigurationByFeatureName(
            @PathVariable String featureName) {
        AiConfigResponse config = aiConfigurationService.getConfigurationByFeatureName(featureName);
        return ResponseEntity.ok(config);
    }

    /**
     * Crea una nueva configuracion de IA.
     * @param request Datos de la configuracion
     * @return Configuracion creada
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AiConfigResponse> createConfiguration(@Valid @RequestBody AiConfigRequest request) {
        AiConfigResponse config = aiConfigurationService.createConfiguration(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(config);
    }

    /**
     * Actualiza una configuracion de IA existente.
     * @param id ID de la configuracion a actualizar
     * @param request Nuevos datos de la configuracion
     * @return Configuracion actualizada
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AiConfigResponse> updateConfiguration(
            @PathVariable Integer id,
            @Valid @RequestBody AiConfigRequest request) {
        AiConfigResponse config = aiConfigurationService.updateConfiguration(id, request);
        return ResponseEntity.ok(config);
    }

    /**
     * Habilita una funcionalidad de IA.
     * @param id ID de la configuracion a habilitar
     * @return Configuracion actualizada
     */
    @PutMapping("/{id}/enable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AiConfigResponse> enableFeature(@PathVariable Integer id) {
        AiConfigResponse config = aiConfigurationService.enableFeature(id);
        return ResponseEntity.ok(config);
    }

    /**
     * Deshabilita una funcionalidad de IA.
     * @param id ID de la configuracion a deshabilitar
     * @return Configuracion actualizada
     */
    @PutMapping("/{id}/disable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AiConfigResponse> disableFeature(@PathVariable Integer id) {
        AiConfigResponse config = aiConfigurationService.disableFeature(id);
        return ResponseEntity.ok(config);
    }
}
