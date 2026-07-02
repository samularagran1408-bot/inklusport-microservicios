package com.inklusport.accesibility;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.Data;
import java.time.Instant;

@Document(collection = "user_preferences")
@Data
public class UserPreferences {

    @Id
    private String id;

    @Indexed(unique = true)
    @Field("user_id")
    private String userId;

    @Indexed
    @Field("disability_type")
    private String disabilityType;

    @Indexed
    private String language;

    @Indexed
    @Field("high_contrast")
    private boolean highContrast;

    @Field("font_size")
    private String fontSize;

    @Indexed
    @Field("screen_reader")
    private boolean screenReader;

    @Field("reduced_motion")
    private boolean reducedMotion;

    @Field("keyboard_navigation")
    private boolean keyboardNavigation;

    @Field("reader_mode")
    private boolean readerMode;

    @Field("notifications_enabled")
    private boolean notificationsEnabled;

    @Field("notification_preferences")
    private NotificationPrefSubDoc notificationPreferences;

    @Field("training_preferences")
    private TrainingPrefSubDoc trainingPreferences;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    // --- SUBDOCUMENTOS ---
    @Data
    public static class NotificationPrefSubDoc {
        private boolean email;
        private boolean push;
        private boolean sms;
    }

    @Data
    public static class TrainingPrefSubDoc {
        @Field("reminder_minutes")
        private int reminderMinutes;
        @Field("voice_instructions")
        private boolean voiceInstructions;
        @Field("simplified_language")
        private boolean simplifiedLanguage;
        @Field("progress_feedback")
        private String progressFeedback;
    }
}