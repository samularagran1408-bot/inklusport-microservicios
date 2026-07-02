package com.inklusport.accesibility;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.Data;
import java.time.Instant;

@Document(collection = "notification_preferences")
@Data
public class NotificationPreferences {

    @Id
    private String id;

    @Indexed(unique = true)
    @Field("user_id")
    private String userId;

    private ChannelsSubDoc channels;

    @Field("event_types")
    private EventTypesSubDoc eventTypes;

    @Field("quiet_hours")
    private QuietHoursSubDoc quietHours;

    @Field("updated_at")
    private Instant updatedAt;

    // --- SUBDOCUMENTOS ---
    @Data
    public static class ChannelsSubDoc {
        private boolean push;
        private boolean email;
        private boolean sms;
        private boolean whatsapp;
    }

    @Data
    public static class EventTypesSubDoc {
        @Field("event_reminder")
        private boolean eventReminder;
        @Field("event_cancelled")
        private boolean eventCancelled;
        @Field("waitlist_offer")
        private boolean waitlistOffer;
        private boolean achievement;
        @Field("system_alert")
        private boolean systemAlert;
    }

    @Data
    public static class QuietHoursSubDoc {
        private boolean enabled;
        private String start; // Formato "22:00"
        private String end;   // Formato "08:00"
    }
}