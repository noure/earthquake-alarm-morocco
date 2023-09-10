package ma.nourlab.earthquakealarm.service;

import ma.nourlab.earthquakealarm.domain.MessageInfo;
import ma.nourlab.earthquakealarm.infrastructure.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Value("${earthquake.message.en}")
    private String messageTemplateEn;

    @Value("${earthquake.message.fr}")
    private String messageTemplateFr;


    public String createMessage(double magnitude, String location, String readableTime, String googleMapsUrl) {
        String messageEn = String.format(messageTemplateEn, magnitude, location, readableTime, googleMapsUrl);
        String messageAr = String.format(messageTemplateFr, magnitude, location, readableTime, googleMapsUrl);

        return messageEn + " | " + messageAr;
    }

    public void saveToDatabase(String readableTime, String messageText) {
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setDatestamp(readableTime);
        messageInfo.setMessage(messageText);
        messageRepository.save(messageInfo);
    }
}
