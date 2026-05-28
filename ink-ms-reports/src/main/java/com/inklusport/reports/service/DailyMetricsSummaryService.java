package com.inklusport.reports.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inklusport.reports.entity.DailyMetricsSummary;
import com.inklusport.reports.repository.DailyMetricsSummaryRepository;

@Service
public class DailyMetricsSummaryService {

    @Autowired
    private DailyMetricsSummaryRepository dailyMetricsSummaryRepository;

    public List<DailyMetricsSummary> getAllMetrics() {
        return dailyMetricsSummaryRepository.findAll();
    }

    public DailyMetricsSummary saveMetric(DailyMetricsSummary metric) {
        return dailyMetricsSummaryRepository.save(metric);
    }
}
