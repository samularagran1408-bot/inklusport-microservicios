package com.inklusport.reports.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_metrics_summary")
public class DailyMetricsSummary {

    @Id
    private String id;

    private LocalDate summaryDate;

    private String metricKey;

    private int metricValue;

    private LocalDateTime updatedAt;

}