package ma.nourlab.earthquakealarm.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Setter
@Getter
public class MessageInfo {
    @Id
    private String datestamp;
    @Column(columnDefinition = "TEXT", length = 5000)
    private String message;
    private String sentMessage;

}
