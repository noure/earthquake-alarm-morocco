package ma.nourlab.earthquakealarm.service;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import ma.nourlab.earthquakealarm.domain.Notification;
import ma.nourlab.earthquakealarm.infrastructure.ngrokclient.NgrokClient;
import ma.nourlab.earthquakealarm.infrastructure.ngrokclient.domain.NgrokResponse;
import ma.nourlab.earthquakealarm.infrastructure.ngrokclient.domain.Tunnel;
import ma.nourlab.earthquakealarm.infrastructure.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@SpringBootTest
public class SendSMSServiceTest {

    @MockBean
    private NgrokClient ngrokClient;

    @MockBean
    private NotificationRepository notificationRepository;
    @MockBean
    private MessageCreator messageCreator;
    @Autowired
    private SMSService smsService;

    @BeforeEach
    public void setUp() {
        /*ngrokClient = mock(NgrokClient.class);
        notificationRepository = mock(NotificationRepository.class);
        */
        messageCreator = mock(MessageCreator.class, RETURNS_DEEP_STUBS);
        //smsService = new SMSService(ngrokClient, notificationRepository);
    }

    //@Test
    public void testSendSms() {
        String phoneNumber = "1234567890";
        String messageText = "Hello, World!";
        long time = 1000L;
        NgrokResponse response = new NgrokResponse();
        response.setTunnels(new ArrayList<>()); // Instantiate the tunnels list

        Tunnel tunnel = new Tunnel();
        tunnel.setProto("http");
        tunnel.setPublic_url("http://example.com");
        response.getTunnels().add(tunnel);

        when(ngrokClient.getTunnels()).thenReturn(response);
        //when(messageCreator.create()).thenReturn(new Message());

        smsService.sendSms(phoneNumber, messageText, time);

        verify(messageCreator, times(1)).create();
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }
}
