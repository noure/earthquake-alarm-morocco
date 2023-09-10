package ma.nourlab.earthquakealarm.infrastructure.ngrokclient.domain;

import lombok.Data;

@Data
public class Tunnel {
    private String name;
    private String public_url;
    private String proto;
}