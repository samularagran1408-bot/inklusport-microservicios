package com.inklusport.reports.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "analytics_events")
public class AnalyticsEvent {

    @Id
    private String id;

    private String eventType;

    private String userId;

    private String module;

    private String metadata;

    private LocalDateTime createdAt;

}