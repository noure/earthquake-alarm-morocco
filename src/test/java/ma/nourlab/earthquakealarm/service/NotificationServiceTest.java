package ma.nourlab.earthquakealarm.service;

import ma.nourlab.earthquakealarm.domain.Notification;
import ma.nourlab.earthquakealarm.infrastructure.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @MockBean
    private NotificationRepository notificationRepository;

    @Test
    public void testUpdateNotificationStatus() throws Exception {
        String sid = "12345";
        String newStatus = "SENT";

        Notification notification = new Notification();
        notification.setMessageSid(sid);

        when(notificationRepository.findByMessageSid(sid)).thenReturn(Optional.of(notification));

        notificationService.updateNotificationStatus(sid, newStatus);

        assertEquals(newStatus, notification.getStatus());
        verify(notificationRepository, times(1)).save(notification);
    }

    @Test
    public void testUpdateNotificationStatus_NotFound() {
        String sid = "12345";
        String newStatus = "SENT";

        when(notificationRepository.findByMessageSid(sid)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> notificationService.updateNotificationStatus(sid, newStatus));

        verify(notificationRepository, never()).save(any(Notification.class));
    }
}
