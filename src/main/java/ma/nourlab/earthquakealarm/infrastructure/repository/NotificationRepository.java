package ma.nourlab.earthquakealarm.infrastructure.repository;

import ma.nourlab.earthquakealarm.domain.Notification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends CrudRepository<Notification, Long> {
   // SentNotification findByLocationAndHourKey(String location, Long hourKey);

    List<Notification> findByPhoneNumberAndMessageContentAndTimestampAfter(String phoneNumber, String messageContent, Long timestamp);

    List<Notification> findByPhoneNumberAndTimestamp(String phoneNumber, long timestamp);

    Optional<Notification> findByMessageSid(String sid);
}