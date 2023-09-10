package ma.nourlab.earthquakealarm.infrastructure.ngrokclient;

import ma.nourlab.earthquakealarm.infrastructure.ngrokclient.domain.NgrokResponse;
import org.json.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "ngrokClient", url = "http://localhost:4040/api")
public interface NgrokClient {

    @GetMapping("/tunnels")
    NgrokResponse getTunnels();
}
