package com.inklusport.reports.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inklusport.reports.entity.ReportConfig;

public interface ReportConfigRepository extends JpaRepository<ReportConfig, String> {
    
}
