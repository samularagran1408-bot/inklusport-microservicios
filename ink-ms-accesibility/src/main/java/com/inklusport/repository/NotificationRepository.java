
package main.java.com.inklusport.repository;
import com.inklusport.auth.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByUserIdAndRead(String userId, boolean read);
}