package ru.se.info.tinder.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.se.info.tinder.dto.SpacesuitRequestMessage;

@Component
@RequiredArgsConstructor
@Log4j2
public class SpacesuitRequestProducer {

    private final KafkaTemplate<String, SpacesuitRequestMessage> kafkaTemplate;

    @Value("${spring.kafka.producer.spacesuit-request-changed-topic}")
    private String spacesuitRequestChangedTopic;

    public void sendMessageToSpacesuitRequestChangedTopic(SpacesuitRequestMessage message) {
        kafkaTemplate.send(spacesuitRequestChangedTopic, message);
        log.info("Sent to Kafka {}: {}", spacesuitRequestChangedTopic, message);
    }
}
