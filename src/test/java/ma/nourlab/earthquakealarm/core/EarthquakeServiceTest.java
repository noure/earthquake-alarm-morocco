package ma.nourlab.earthquakealarm.core;
import ma.nourlab.earthquakealarm.infrastructure.geoscienceclient.EarthquakeClient;
import ma.nourlab.earthquakealarm.infrastructure.googlemap.GoogleMapUrlGenerator;
import ma.nourlab.earthquakealarm.infrastructure.repository.NotificationRepository;
import ma.nourlab.earthquakealarm.service.MessageService;
import ma.nourlab.earthquakealarm.service.ThrottledSmsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EarthquakeServiceTest {

    private ThrottledSmsService throttledSmsService;
    private EarthquakeClient earthquakeClient;
    private NotificationRepository notificationRepository;
    private MessageService messageService;
    private GoogleMapUrlGenerator googleMapsService;
    private EarthquakeService earthquakeService;

    @BeforeEach
    public void setUp() {
        throttledSmsService = mock(ThrottledSmsService.class);
        earthquakeClient = mock(EarthquakeClient.class);
        notificationRepository = mock(NotificationRepository.class);
        messageService = mock(MessageService.class);
        googleMapsService = mock(GoogleMapUrlGenerator.class);
        earthquakeService = new EarthquakeService(throttledSmsService, earthquakeClient, notificationRepository, messageService, googleMapsService);
        earthquakeService.phoneNumbers = Arrays.asList("1234567890", "0987654321");
        when(messageService.createMessage(anyDouble(), anyString(), anyString(), anyString())).thenReturn("some message");
        when(googleMapsService.generateGoogleMapsUrl(anyDouble(), anyDouble())).thenReturn("http://example.com");

    }

    @Test
    public void testFetchAndProcessEarthquakeData() throws Exception {
        String earthquakeData = readJsonFromFile("earthquake_data.json");
        when(earthquakeClient.fetchEarthquakeData()).thenReturn(earthquakeData);

        earthquakeService.fetchAndProcessEarthquakeData();

        verify(earthquakeClient, times(1)).fetchEarthquakeData();
        verify(throttledSmsService, atLeastOnce()).sendMessageThrottled(anyString(), anyString(), anyLong());
    }

    private String readJsonFromFile(String filename) throws  FileNotFoundException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(filename);
        if (is == null) {
            throw new FileNotFoundException("File not found: " + filename);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        return reader.lines().collect(Collectors.joining("\n"));
    }

}
