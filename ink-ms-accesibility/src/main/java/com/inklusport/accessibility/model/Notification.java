package com.inklusport.accessibility.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Document(collection = "notifications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@CompoundIndex(name = "user_read_idx", def = "{'userId': 1, 'read': 1}")
public class Notification {

    @Id
    private String id;

    @Indexed
    private String userId;

    private String type;
    private String title;
    private String body;
    private String eventId;
    private String priority;

    private Map<String, Object> adaptations;
    private List<String> deliveryMethods;

    private Boolean read;
    private LocalDateTime readAt;
    private Map<String, Boolean> deliveryStatus;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime scheduledFor;
    private LocalDateTime expiresAt;
}