package com.inklusport.reports.controller;

import com.inklusport.reports.dto.ReportConfigRequest;
import com.inklusport.reports.dto.ReportConfigResponse;
import com.inklusport.reports.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    /**
     * Inyección de Servicio
     */
    private final ReportService reportService;

    /**
     * Crear Reporte 
     * @param userId
     * @param request
     * @return
     */
    @PostMapping("/configs")
    public ResponseEntity<ReportConfigResponse> createReportConfig(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody ReportConfigRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reportService.createReportConfig(userId, request));
    }

    /**
     * Obtener mis propios reportes
     * @param userId
     * @return
     */
    @GetMapping("/configs")
    public ResponseEntity<List<ReportConfigResponse>> getMyReportConfigs(@AuthenticationPrincipal String userId) {
        return ResponseEntity.ok(reportService.getMyReportConfigs(userId));
    }

    /**
     * Eliminar reportes por id
     * @param userId
     * @param id
     * @return
     */
    @DeleteMapping("/configs/{id}")
    public ResponseEntity<Void> deleteReportConfig(
            @AuthenticationPrincipal String userId,
            @PathVariable String id) {
        reportService.deleteReportConfig(id, userId);
        return ResponseEntity.noContent().build();
    }
}