package com.inklusport.admin.repository;

import com.inklusport.admin.entity.ScheduledReport;
import com.inklusport.admin.enums.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduledReportRepository extends JpaRepository<ScheduledReport, String> {
    
    List<ScheduledReport> findByIsActiveTrue();
    
    List<ScheduledReport> findByType(ReportType type);
    
    @Query("SELECT s FROM ScheduledReport s WHERE s.isActive = true AND s.nextRun <= CURRENT_TIMESTAMP")
    List<ScheduledReport> findPendingReports();
    
    @Modifying
    @Transactional
    @Query("UPDATE ScheduledReport s SET s.lastRun = :lastRun, s.nextRun = :nextRun WHERE s.id = :id")
    void updateRunTimes(@Param("id") String id, 
                        @Param("lastRun") LocalDateTime lastRun, 
                        @Param("nextRun") LocalDateTime nextRun);
}