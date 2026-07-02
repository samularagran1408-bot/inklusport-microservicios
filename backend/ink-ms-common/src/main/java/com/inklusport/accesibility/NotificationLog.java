package com.inklusport.accesibility;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.Data;
import java.time.Instant;

@Document(collection = "notification_log")
@Data
@CompoundIndex(name = "user_sent_idx", def = "{'user_id': 1, 'sent_at': -1}")
public class NotificationLog {

    @Id
    private String id;

    @Field("user_id")
    private String userId;

    @Indexed
    @Field("notification_id")
    private String notificationId; 
    @Field("sent_at")
    private Instant sentAt;

    @Field("delivery_method")
    private String deliveryMethod;

    @Indexed
    private String status;

    private String error;

    @Field("retry_count")
    private int retryCount;
}