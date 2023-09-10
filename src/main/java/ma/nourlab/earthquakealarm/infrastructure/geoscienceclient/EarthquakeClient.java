package ma.nourlab.earthquakealarm.infrastructure.geoscienceclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "earthquakeClient", url = "https://earthquake.usgs.gov")
public interface EarthquakeClient {

    @GetMapping("/earthquakes/feed/v1.0/summary/2.5_week.geojson")
    String fetchEarthquakeData();
}
