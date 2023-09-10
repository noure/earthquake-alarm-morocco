package ma.nourlab.earthquakealarm.service;


import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import ma.nourlab.earthquakealarm.domain.Notification;
import ma.nourlab.earthquakealarm.infrastructure.ngrokclient.NgrokClient;
import ma.nourlab.earthquakealarm.infrastructure.ngrokclient.domain.NgrokResponse;
import ma.nourlab.earthquakealarm.infrastructure.ngrokclient.domain.Tunnel;
import ma.nourlab.earthquakealarm.infrastructure.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SMSServiceTest {

    @Autowired
    private SMSService smsService;

    @MockBean
    private NgrokClient ngrokClient;

    @MockBean
    private NotificationRepository sentNotificationRepository;
    @MockBean
    private Message messageCreator;
    @Captor
    private ArgumentCaptor<Message> createMessageOptionsCaptor;




    @Test
    public void testGetPublicUrl() {
        NgrokResponse response = new NgrokResponse();
        response.setTunnels(new ArrayList<>()); // Instantiate the tunnels list

        Tunnel tunnel = new Tunnel();
        tunnel.setProto("http");
        tunnel.setPublic_url("http://example.com");
        response.getTunnels().add(tunnel);

        when(ngrokClient.getTunnels()).thenReturn(response);

        String publicUrl = smsService.getPublicUrl();

        assertEquals("http://example.com", publicUrl);
    }

}
