package com.inklusport.accesibility;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.Data;
import java.time.Instant;
import java.util.List;

@Document(collection = "notifications")
@Data
// ÍNDICES COMPUESTOS: user_id + created_at DESC (-1) y user_id + read
@CompoundIndexes({
    @CompoundIndex(name = "user_created_idx", def = "{'user_id': 1, 'created_at': -1}"),
    @CompoundIndex(name = "user_read_idx", def = "{'user_id': 1, 'read': 1}")
})
public class Notification {

    @Id
    private String id;

    @Field("user_id")
    private String userId;

    @Indexed
    private String type;

    private String title;
    private String body;

    @Field("event_id")
    private String eventId;

    @Indexed
    private String priority;

    private AdaptationsSubDoc adaptations;

    @Field("delivery_methods")
    private List<String> deliveryMethods;

    private boolean read;

    @Field("read_at")
    private Instant readAt;

    @Field("delivery_status")
    private DeliveryStatusSubDoc deliveryStatus;

    @Field("created_at")
    private Instant createdAt;

    @Indexed
    @Field("scheduled_for")
    private Instant scheduledFor;

    @Indexed(expireAfterSeconds = 0)
    @Field("expires_at")
    private Instant expiresAt;

    @Data
    public static class AdaptationsSubDoc {
        private VisualDoc visual;
        private AuditoryDoc auditory;
        private CognitiveDoc cognitive;

        @Data
        public static class VisualDoc {
            @Field("screen_reader_optimized")
            private boolean screenReaderOptimized;
            @Field("high_contrast")
            private boolean highContrast;
        }

        @Data
        public static class AuditoryDoc {
            @Field("flash_alert")
            private boolean flashAlert;
        }

        @Data
        public static class CognitiveDoc {
            @Field("simplified_text")
            private String simplifiedText;
            private boolean emojis;
        }
    }

    @Data
    public static class DeliveryStatusSubDoc {
        @Field("email_sent")
        private boolean emailSent;
        @Field("push_sent")
        private boolean pushSent;
        @Field("sms_sent")
        private boolean smsSent;
    }
}