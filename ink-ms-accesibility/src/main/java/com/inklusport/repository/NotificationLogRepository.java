package main.java.com.inklusport.repository;
import com.inklusport.auth.entity.NotificationLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationLogRepository extends MongoRepository<NotificationLog, String> {
}