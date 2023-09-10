package ma.nourlab.earthquakealarm.infrastructure.ngrokclient.domain;


import lombok.Data;

import java.util.List;

@Data
public class NgrokResponse {
    private List<Tunnel> tunnels;

}

