package ma.nourlab.earthquakealarm;


import lombok.extern.slf4j.Slf4j;
import ma.nourlab.earthquakealarm.infrastructure.repository.NotificationRepository;
import ma.nourlab.earthquakealarm.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j

public class SmsStatusCallbackController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/api/smsStatusCallback")
    public void handleStatusCallback(@RequestParam("MessageSid") String messageSid,
                                     @RequestParam("MessageStatus") String messageStatus) throws Exception {
        // Logic to handle status
        log.info("Received status " + messageStatus + " for message id " + messageSid);

        notificationService.updateNotificationStatus(messageSid, messageStatus);
    }
}
