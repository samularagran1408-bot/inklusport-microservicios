package com.inklusport.reports.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "report_configs")
public class ReportConfig {

    @Id
    private String id;

    private String reportName;

    private String filters;

    private String ownerId;

    private LocalDateTime lastRun;

    private LocalDateTime createdAt;

}