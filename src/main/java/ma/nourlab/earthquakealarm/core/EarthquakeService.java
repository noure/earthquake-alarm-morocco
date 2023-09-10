package ma.nourlab.earthquakealarm.core;

import lombok.extern.slf4j.Slf4j;
import ma.nourlab.earthquakealarm.domain.Notification;
import ma.nourlab.earthquakealarm.infrastructure.geoscienceclient.EarthquakeClient;
import ma.nourlab.earthquakealarm.infrastructure.googlemap.GoogleMapUrlGenerator;
import ma.nourlab.earthquakealarm.infrastructure.repository.NotificationRepository;
import ma.nourlab.earthquakealarm.service.MessageService;
import ma.nourlab.earthquakealarm.service.ThrottledSmsService;
import ma.nourlab.earthquakealarm.util.DateUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

@Service
@Slf4j
public class EarthquakeService {

    @Autowired
    private ThrottledSmsService throttledSmsService;

    @Autowired
    private EarthquakeClient earthquakeClient;

    @Autowired
    private NotificationRepository sentNotificationRepository;

    @Autowired
    private MessageService messageService;

    @Autowired
    private GoogleMapUrlGenerator googleMapsService;

    @Value("#{'${twilio.phone.numbers}'.split(',')}")
    List<String> phoneNumbers;

    public EarthquakeService(ThrottledSmsService throttledSmsService, EarthquakeClient earthquakeClient, NotificationRepository notificationRepository, MessageService messageService, GoogleMapUrlGenerator googleMapsService) {
        this.throttledSmsService = throttledSmsService;
        this.earthquakeClient = earthquakeClient;
        this.sentNotificationRepository = notificationRepository;
        this.messageService = messageService;
        this.googleMapsService = googleMapsService;
    }

    @Scheduled(fixedDelay = 60000)
    public void fetchAndProcessEarthquakeData() throws Exception {
        JSONArray features = fetchEarthquakeData();
        processEarthquakeData(features);
    }

    private JSONArray fetchEarthquakeData() {
        String response = earthquakeClient.fetchEarthquakeData();
       // log.info("Response from earthquake.usgs.gov: {}", response);
        log.info("Got response from earthquake.usgs.gov");
        JSONObject jsonResponse = new JSONObject(response);
        return jsonResponse.getJSONArray("features");
    }

    private void processEarthquakeData(JSONArray features) {
        log.info("Processing {} earthquakes", features.length());
        IntStream.range(0, features.length()).forEach(i -> {
            JSONObject feature = features.getJSONObject(i);
            processSingleEarthquake(feature);
        });
    }

    private void processSingleEarthquake(JSONObject feature) {
        JSONObject properties = feature.getJSONObject("properties");
        JSONObject geometry = feature.getJSONObject("geometry");
        JSONArray coordinates = geometry.getJSONArray("coordinates");
        double longitude = coordinates.getDouble(0);
        double latitude = coordinates.getDouble(1);
        String location = properties.getString("place");
        double magnitude = properties.getDouble("mag");
        long time = properties.getLong("time");

        Date date = new Date(time);
        String readableTime = DateUtil.formatDate(date);

        if (location.contains("Morocco")) {
            String googleMapsUrl = googleMapsService.generateGoogleMapsUrl(latitude, longitude);
            String messageText = messageService.createMessage(magnitude, location, readableTime, googleMapsUrl);

            log.info("Saving message: {}", messageText);
            messageService.saveToDatabase(readableTime, messageText);
            log.info("Sending message: {}", messageText);
            phoneNumbers.forEach(phoneNumber ->{
                if (!hasNotificationBeenSent(phoneNumber, time)) {
                    throttledSmsService.sendMessageThrottled(phoneNumber, messageText, time);
                }
            });
        }
    }
    private boolean hasNotificationBeenSent(String phoneNumber, long timestamp) {
        List<Notification> records = sentNotificationRepository.findByPhoneNumberAndTimestamp(phoneNumber, timestamp);
        return !records.isEmpty();
    }

}
