package com.inklusport.users.repository;

import com.inklusport.users.entity.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, String> {

    List<UserActivity> findByUserIdOrderByCreatedAtDesc(String userId);

    @Query("SELECT COUNT(a) FROM UserActivity a WHERE a.user.id = :userId AND a.createdAt > :since")
    long countRecentActivitiesByUserId(@Param("userId") String userId, @Param("since") LocalDateTime since);
}