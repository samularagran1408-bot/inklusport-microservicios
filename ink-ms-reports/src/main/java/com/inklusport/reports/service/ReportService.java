package com.inklusport.reports.service;

import com.inklusport.reports.dto.ReportConfigRequest;
import com.inklusport.reports.dto.ReportConfigResponse;
import com.inklusport.reports.entity.ReportConfig;
import com.inklusport.reports.repository.ReportConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    /**
     * Inyección de Repositorios
     */
    private final ReportConfigRepository reportConfigRepository;

    @Transactional
    public ReportConfigResponse createReportConfig(String ownerId, ReportConfigRequest request) {
        ReportConfig config = ReportConfig.builder()
                .reportName(request.getReportName())
                .filters(request.getFilters())
                .ownerId(ownerId)
                .build();

        ReportConfig saved = reportConfigRepository.save(config);
        log.info("Configuración de reporte creada: {} para usuario {}", saved.getReportName(), ownerId);

        return convertToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ReportConfigResponse> getMyReportConfigs(String ownerId) {
        return reportConfigRepository.findByOwnerId(ownerId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteReportConfig(String id, String ownerId) {
        ReportConfig config = reportConfigRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Configuración no encontrada"));

        if (!config.getOwnerId().equals(ownerId)) {
            throw new RuntimeException("No autorizado");
        }

        reportConfigRepository.delete(config);
        log.info("Configuración de reporte eliminada: {}", id);
    }

    private ReportConfigResponse convertToResponse(ReportConfig config) {
        return ReportConfigResponse.builder()
                .id(config.getId())
                .reportName(config.getReportName())
                .filters(config.getFilters())
                .ownerId(config.getOwnerId())
                .lastRun(config.getLastRun())
                .createdAt(config.getCreatedAt())
                .build();
    }
}