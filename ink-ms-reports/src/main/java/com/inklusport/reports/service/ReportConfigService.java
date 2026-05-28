package com.inklusport.reports.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inklusport.reports.dto.request.CreateReportRequest;
import com.inklusport.reports.dto.response.ReportResponse;
import com.inklusport.reports.entity.ReportConfig;
import com.inklusport.reports.repository.ReportConfigRepository;

@Service
public class ReportConfigService {

    @Autowired
    private ReportConfigRepository reportConfigRepository;

    public List<ReportConfig> getAllReports() {
        return reportConfigRepository.findAll();
    }

    public ReportResponse createReport(CreateReportRequest request) {

        ReportConfig report = new ReportConfig();

        report.setReportName(request.getReportName());
        report.setFilters(request.getFilters());
        report.setOwnerId(request.getOwnerId());

        ReportConfig savedReport = reportConfigRepository.save(report);

        ReportResponse response = new ReportResponse();

        response.setId(savedReport.getId());
        response.setReportName(savedReport.getReportName());
        response.setFilters(savedReport.getFilters());
        response.setOwnerId(savedReport.getOwnerId());

        return response;
    }
}