package ma.nourlab.earthquakealarm.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String phoneNumber;
    @Column(columnDefinition = "TEXT")
    private String messageContent;
    private Long timestamp;
    private String messageSid;
    private String status;
}
