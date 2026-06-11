package com.inklusport.reports.service;

import com.inklusport.reports.client.SportsServiceClient;
import com.inklusport.reports.client.UserServiceClient;
import com.inklusport.reports.dto.DashboardFilters;
import com.inklusport.reports.dto.DashboardResponse;
import com.inklusport.reports.repository.AnalyticsEventRepository;
import com.inklusport.reports.repository.DailyMetricsSummaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final DailyMetricsSummaryRepository metricsRepository;
    private final UserServiceClient userServiceClient;
    private final SportsServiceClient sportsServiceClient;

    public DashboardResponse getDashboard(DashboardFilters filters) {
        LocalDateTime startDate = filters.getStartDate() != null ? 
                filters.getStartDate().atStartOfDay() : 
                LocalDateTime.now().minusDays(30);
        LocalDateTime endDate = filters.getEndDate() != null ? 
                filters.getEndDate().atTime(LocalTime.MAX) : 
                LocalDateTime.now();

        int totalUsers;
        try {
            totalUsers = userServiceClient.getAllUsers().size();
            log.info("Total de usuarios obtenido: {}", totalUsers);
        } catch (Exception e) {
            log.error("Error al obtener total de usuarios: {}", e.getMessage());
            totalUsers = 0;
        }

        /**
         * Métricas principales
         */
        Map<String, Integer> metrics = new HashMap<>();
        metrics.put("total_users", totalUsers);
        metrics.put("active_events", getActiveEventsCount());
        metrics.put("total_sports", getTotalSports());

        /**
         * Conteo de eventos por tipo
         */
        List<Object[]> eventCountsRaw = analyticsEventRepository.countByEventTypeAndDateRange(startDate, endDate);
        Map<String, Long> eventCounts = eventCountsRaw.stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Long) row[1]
                ));

        /**
         * Tendencia semanal
         */
        Map<String, Integer> weeklyTrend = new HashMap<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            int count = metricsRepository.findBySummaryDateAndMetricKey(date, "total_events")
                    .map(metric -> metric.getMetricValue())
                    .orElseGet(() -> (int) analyticsEventRepository.countByDateRange(
                            date.atStartOfDay(), date.atTime(LocalTime.MAX)));
            weeklyTrend.put(date.toString(), count);
        }

        return DashboardResponse.builder()
                .metrics(metrics)
                .eventCounts(eventCounts)
                .weeklyTrend(weeklyTrend)
                .build();
    }

    private int getActiveEventsCount() {
        try {
            return sportsServiceClient.getActiveEventsCount();
        } catch (Exception e) {
            log.error("Error al obtener eventos activos: {}", e.getMessage());
            return 0;
        }
    }

    private int getTotalSports() {
        try {
            return sportsServiceClient.getTotalSports();
        } catch (Exception e) {
            log.error("Error al obtener total de deportes: {}", e.getMessage());
            return 0;
        }
    }
}