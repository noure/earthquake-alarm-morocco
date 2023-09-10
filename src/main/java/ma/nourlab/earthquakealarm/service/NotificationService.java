package ma.nourlab.earthquakealarm.service;

import lombok.extern.slf4j.Slf4j;
import ma.nourlab.earthquakealarm.domain.Notification;
import ma.nourlab.earthquakealarm.infrastructure.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public void updateNotificationStatus(String sid, String newStatus) throws Exception {
        Notification notification = notificationRepository.findByMessageSid(sid.toString()).orElseThrow(() -> new Exception("Notification not found"));
        notification.setStatus(newStatus);
        notificationRepository.save(notification);
        log.info("Updated notification status to " + newStatus + " for notification id " + sid);
    }
}
