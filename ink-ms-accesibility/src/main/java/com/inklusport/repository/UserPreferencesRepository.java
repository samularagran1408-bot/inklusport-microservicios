package main.java.com.inklusport.repository;
import com.inklusport.auth.entity.UserPreferences;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserPreferencesRepository extends MongoRepository<UserPreferences, String> {
    Optional<UserPreferences> findByUserId(String userId);
}