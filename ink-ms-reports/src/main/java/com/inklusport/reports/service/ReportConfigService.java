package com.inklusport.reports.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inklusport.reports.entity.ReportConfig;
import com.inklusport.reports.repository.ReportConfigRepository;

@Service
public class ReportConfigService {

    @Autowired
    private ReportConfigRepository reportConfigRepository;

    public List<ReportConfig> getAllReports() {
        return reportConfigRepository.findAll();
    }

    public ReportConfig saveReport(ReportConfig reportConfig) {
        return reportConfigRepository.save(reportConfig);
    }
}