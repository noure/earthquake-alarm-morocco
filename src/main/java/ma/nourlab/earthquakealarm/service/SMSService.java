package ma.nourlab.earthquakealarm.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import ma.nourlab.earthquakealarm.domain.Notification;
import ma.nourlab.earthquakealarm.infrastructure.ngrokclient.NgrokClient;
import ma.nourlab.earthquakealarm.infrastructure.ngrokclient.domain.NgrokResponse;
import ma.nourlab.earthquakealarm.infrastructure.ngrokclient.domain.Tunnel;
import ma.nourlab.earthquakealarm.infrastructure.repository.NotificationRepository;
import net.minidev.json.writer.JsonReader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Service
@Slf4j
public class SMSService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;


    @Autowired
    protected NgrokClient ngrokClient;

    @Autowired
    protected NotificationRepository sentNotificationRepository;

    public SMSService(NgrokClient ngrokClient, NotificationRepository notificationRepository) {
   this.ngrokClient = ngrokClient;
    this.sentNotificationRepository = notificationRepository;

    }

    // Initialize Twilio
    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }


    public void sendSms(String phoneNumber, String messageText, long time) {

            log.info("Sending message to {}", phoneNumber);
            Message message = Message.creator(
                    new PhoneNumber(phoneNumber),
                    new PhoneNumber("whatsapp:+14155238886"), messageText)
                    .setStatusCallback(getPublicUrl() + "/api/smsStatusCallback")
                    .create();
            log.info("Message sent to {} with sid {}", phoneNumber, message.getSid());
        markNotificationAsSent(phoneNumber, messageText, time, message.getSid());

    }


    private void markNotificationAsSent(String phoneNumber, String message, long timestamp, String sid) {
        Notification record = new Notification();
        record.setPhoneNumber(phoneNumber);
        record.setMessageContent(message);
        record.setTimestamp(timestamp);
        record.setMessageSid(sid);
        sentNotificationRepository.save(record);
        log.info("Notification saved to database");
    }

    public String getPublicUrl() {


        NgrokResponse response = ngrokClient.getTunnels();
        String publicUrl = response.getTunnels().stream()
                .filter(tunnel -> "http".equals(tunnel.getProto()) || "https".equals(tunnel.getProto()))
                .findFirst()
                .map(Tunnel::getPublic_url)
                .orElseThrow(() -> new RuntimeException("No HTTP/HTTPS tunnel found"));
        log.info("Public URL: {}", publicUrl);
        return publicUrl;
    }
}
