package com.inklusport.reports.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inklusport.reports.entity.AnalyticsEvent;
import com.inklusport.reports.repository.AnalyticsEventRepository;

@Service
public class AnalyticsEventService {

    @Autowired
    private AnalyticsEventRepository analyticsEventRepository;

    public List<AnalyticsEvent> getAllEvents() {
        return analyticsEventRepository.findAll();
    }

    public AnalyticsEvent saveEvent(AnalyticsEvent event) {
        return analyticsEventRepository.save(event);
    }
}
