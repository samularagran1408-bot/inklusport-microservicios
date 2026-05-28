package main.java.com.inklusport.repository;
import com.inklusport.auth.entity.NotificationPreferences;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface NotificationPreferencesRepository extends MongoRepository<NotificationPreferences, String> {
    Optional<NotificationPreferences> findByUserId(String userId);
}
