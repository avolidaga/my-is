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

    @Value("${spring.kafka.producer.spacesuit-request-created-topic}")
    private String spacesuitRequestCreatedTopic;

    public void sendMessageToSpacesuitRequestChangedTopic(SpacesuitRequestMessage message) {
        log.info("Sent to Kafka {}: {}", spacesuitRequestChangedTopic, message);
        kafkaTemplate.send(spacesuitRequestChangedTopic, message);
    }

    public void sendMessageToSpacesuitRequestCreatedTopic(SpacesuitRequestMessage message) {
        log.info("Sent to Kafka {}: {}", spacesuitRequestCreatedTopic, message);
        kafkaTemplate.send(spacesuitRequestCreatedTopic, message);
    }
}
