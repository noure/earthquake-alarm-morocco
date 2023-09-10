package ma.nourlab.earthquakealarm.service;

import ma.nourlab.earthquakealarm.domain.MessageInfo;
import ma.nourlab.earthquakealarm.infrastructure.repository.MessageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    @MockBean
    private MessageRepository messageRepository;

    @Test
    public void testCreateMessage() {
        double magnitude = 7.5;
        String location = "San Francisco";
        String readableTime = "2022-01-01 12:00:00";
        String googleMapsUrl = "https://maps.google.com";

        String expected="Recent Earthquake of M 7.5 strikes off San Francisco at 2022-01-01 12:00:00. For more info: https://maps.google.com | Rï¿½cents tremblements de terre de magnitude 7.5 frappent San Francisco ï¿½ 2022-01-01 12:00:00. Pour plus d'informations: https://maps.google.com";
        String result = messageService.createMessage(magnitude, location, readableTime, googleMapsUrl);

        assertEquals(expected, result);
    }

    @Test
    public void testSaveToDatabase() {
        String readableTime = "2022-01-01 12:00:00";
        String messageText = "Test message";

        messageService.saveToDatabase(readableTime, messageText);

        verify(messageRepository, times(1)).save(any(MessageInfo.class));
    }
}
