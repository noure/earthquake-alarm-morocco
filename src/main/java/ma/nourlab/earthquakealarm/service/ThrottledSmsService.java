package ma.nourlab.earthquakealarm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class ThrottledSmsService {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Autowired
    private  SMSService smsService;

    public ThrottledSmsService(SMSService smsService) {
        this.smsService = smsService;
    }

    public void sendMessageThrottled(String phoneNumber, String message, long time) {
        scheduler.schedule(() -> smsService.sendSms(phoneNumber, message, time), 1, TimeUnit.SECONDS);
    }
}
