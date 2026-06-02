package com.inklusport.reports.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inklusport.reports.dto.request.CreateReportRequest;
import com.inklusport.reports.dto.response.ReportResponse;
import com.inklusport.reports.entity.ReportConfig;
import com.inklusport.reports.service.ReportConfigService;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportConfigService reportConfigService;

    @GetMapping
    public List<ReportConfig> getAllReports() {
        return reportConfigService.getAllReports();
    }

    @PostMapping
    public ResponseEntity<ReportResponse> createReport(@RequestBody CreateReportRequest request) {
        ReportResponse response = reportConfigService.createReport(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
