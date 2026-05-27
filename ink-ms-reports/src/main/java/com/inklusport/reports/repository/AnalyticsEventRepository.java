package com.inklusport.reports.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inklusport.reports.entity.AnalyticsEvent;

public interface AnalyticsEventRepository extends JpaRepository<AnalyticsEvent, String> {




}
