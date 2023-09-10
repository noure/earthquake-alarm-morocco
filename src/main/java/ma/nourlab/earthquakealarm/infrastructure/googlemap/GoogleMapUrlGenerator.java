package ma.nourlab.earthquakealarm.infrastructure.googlemap;


import org.springframework.stereotype.Service;

@Service
public class GoogleMapUrlGenerator {

    public String generateGoogleMapsUrl(double latitude, double longitude) {
        return "https://www.google.com/maps?q=" + latitude + "," + longitude;
    }

}
