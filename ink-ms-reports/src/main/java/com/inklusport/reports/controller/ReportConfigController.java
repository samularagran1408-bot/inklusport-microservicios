package com.inklusport.reports.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.inklusport.reports.entity.ReportConfig;
import com.inklusport.reports.service.ReportConfigService;

@RestController
@RequestMapping("/reports")
public class ReportConfigController {

    @Autowired
    private ReportConfigService reportConfigService;

    @GetMapping
    public List<ReportConfig> getAllReports() {
        return reportConfigService.getAllReports();
    }

    @PostMapping
    public ReportConfig saveReport(@RequestBody ReportConfig reportConfig) {
        return reportConfigService.saveReport(reportConfig);
    }
}