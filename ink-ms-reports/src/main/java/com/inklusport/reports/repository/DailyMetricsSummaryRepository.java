package com.inklusport.reports.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inklusport.reports.entity.DailyMetricsSummary;

public interface DailyMetricsSummaryRepository extends JpaRepository<DailyMetricsSummary, String> {
    
}
