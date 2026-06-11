package com.inklusport.admin.controller;

import com.inklusport.admin.dto.SystemConfigRequest;
import com.inklusport.admin.dto.SystemConfigResponse;
import com.inklusport.admin.service.SystemConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para la gestion de parametros de configuracion del sistema.
 * Permite obtener, crear y actualizar parametros globales de la plataforma.
 */
@RestController
@RequestMapping("/api/admin/config")
@RequiredArgsConstructor
public class SystemConfigController {

    private final SystemConfigService systemConfigService;

    /**
     * Obtiene todos los parametros de configuracion del sistema.
     * @return Lista de configuraciones
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SystemConfigResponse>> getAllConfigs() {
        List<SystemConfigResponse> configs = systemConfigService.getAllConfigs();
        return ResponseEntity.ok(configs);
    }

    /**
     * Obtiene una configuracion especifica por su clave.
     * @param configKey Clave de configuracion
     * @return Configuracion encontrada
     */
    @GetMapping("/{configKey}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SystemConfigResponse> getConfigByKey(@PathVariable String configKey) {
        SystemConfigResponse config = systemConfigService.getConfigByKey(configKey);
        return ResponseEntity.ok(config);
    }

    /**
     * Crea una nueva configuracion del sistema.
     * @param request Datos de la configuracion
     * @return Configuracion creada
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SystemConfigResponse> createConfig(@Valid @RequestBody SystemConfigRequest request) {
        SystemConfigResponse config = systemConfigService.createConfig(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(config);
    }

    /**
     * Actualiza una configuracion del sistema existente.
     * @param configKey Clave de la configuracion a actualizar
     * @param request Nuevos datos de la configuracion
     * @return Configuracion actualizada
     */
    @PutMapping("/{configKey}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SystemConfigResponse> updateConfig(
            @PathVariable String configKey,
            @Valid @RequestBody SystemConfigRequest request) {
        SystemConfigResponse config = systemConfigService.updateConfig(configKey, request);
        return ResponseEntity.ok(config);
    }

    /**
     * Elimina una configuracion del sistema.
     * @param configKey Clave de la configuracion a eliminar
     * @return Confirmacion de eliminacion
     */
    @DeleteMapping("/{configKey}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteConfig(@PathVariable String configKey) {
        systemConfigService.deleteConfig(configKey);
        return ResponseEntity.noContent().build();
    }
}
