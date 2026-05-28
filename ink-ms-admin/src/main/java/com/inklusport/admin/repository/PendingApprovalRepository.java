package com.inklusport.admin.repository;

import com.inklusport.admin.entity.PendingApproval;
import com.inklusport.admin.enums.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PendingApprovalRepository extends JpaRepository<PendingApproval, String> {
    
    Page<PendingApproval> findByStatus(RequestStatus status, Pageable pageable);
    
    Page<PendingApproval> findByTargetType(String targetType, Pageable pageable);
    
    Page<PendingApproval> findByRequestedBy(String requestedBy, Pageable pageable);
    
    @Query("SELECT p FROM PendingApproval p WHERE p.status = 'pending' AND p.requestedAt <= :cutoff")
    List<PendingApproval> findPendingOlderThan(@Param("cutoff") java.time.LocalDateTime cutoff);
    
    @Modifying
    @Transactional
    @Query("UPDATE PendingApproval p SET p.status = :status, p.reviewedBy = :reviewedBy, p.reviewedAt = CURRENT_TIMESTAMP, p.reviewNotes = :notes WHERE p.id = :id")
    void updateStatus(@Param("id") String id, 
                      @Param("status") RequestStatus status, 
                      @Param("reviewedBy") String reviewedBy,
                      @Param("notes") String notes);
}