package ma.nourlab.earthquakealarm.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ThrottledSmsServiceTest {

    private SMSService smsService;
    private ThrottledSmsService throttledSmsService;

    @BeforeEach
    public void setUp() {
        smsService = mock(SMSService.class);
        throttledSmsService = new ThrottledSmsService(smsService);
    }

    @Test
    public void testSendMessageThrottled() throws InterruptedException {
        String phoneNumber = "1234567890";
        String message = "Hello, World!";
        long time = 1000L;

        throttledSmsService.sendMessageThrottled(phoneNumber, message, time);

        // Wait for the scheduler to execute the task
        TimeUnit.SECONDS.sleep(2);

        verify(smsService, times(1)).sendSms(eq(phoneNumber), eq(message), eq(time));
    }
}
