package com.inklusport.auth.repository;

import com.inklusport.auth.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {
    
  Optional<PasswordResetToken> findByTokenAndUsedFalse(String token);
  
  @Modifying
  @Transactional
  @Query("DELETE FROM PasswordResetToken t WHERE t.userId = :userId")
  void deleteByUserId(String userId);
}