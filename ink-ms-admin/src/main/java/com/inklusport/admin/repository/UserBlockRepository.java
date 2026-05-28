package com.inklusport.admin.repository;

import com.inklusport.admin.entity.UserBlock;
import com.inklusport.admin.enums.BlockType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserBlockRepository extends JpaRepository<UserBlock, String> {
    
    Optional<UserBlock> findByUserIdAndIsActiveTrue(String userId);
    
    Page<UserBlock> findByIsActiveTrue(Pageable pageable);
    
    Page<UserBlock> findByBlockType(BlockType blockType, Pageable pageable);
    
    @Query("SELECT b FROM UserBlock b WHERE b.isActive = true AND b.blockType = 'temporal' AND b.expiresAt < CURRENT_TIMESTAMP")
    List<UserBlock> findExpiredBlocks();
    
    @Modifying
    @Transactional
    @Query("UPDATE UserBlock b SET b.isActive = false, b.unblockedAt = CURRENT_TIMESTAMP WHERE b.id = :id")
    void deactivateBlock(@Param("id") String id);
    
    @Modifying
    @Transactional
    @Query("UPDATE UserBlock b SET b.isActive = false WHERE b.userId = :userId AND b.isActive = true")
    void deactivateAllUserBlocks(@Param("userId") String userId);
}