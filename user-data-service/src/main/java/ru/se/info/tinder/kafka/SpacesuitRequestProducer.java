package ru.se.info.tinder.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.se.info.tinder.dto.ProfileImageMessage;

@Component
@RequiredArgsConstructor
@Log4j2
public class SpacesuitRequestProducer {

    private final KafkaTemplate<String, ProfileImageMessage> kafkaTemplate;

    @Value("${spring.kafka.producer.profile-image-created-topic}")
    private String profileImageCreatedTopic;

    @Value("${spring.kafka.producer.profile-image-deleted-topic}")
    private String profileImageDeletedTopic;

    public void sendMessageToProfileImageCreatedTopic(ProfileImageMessage message) {
        log.info("Sent to Kafka {}: {}", profileImageCreatedTopic, message);
        kafkaTemplate.send(profileImageCreatedTopic, message);
    }

    public void sendMessageToProfileImageDeletedTopic(ProfileImageMessage message) {
        log.info("Sent to Kafka {}: {}", profileImageDeletedTopic, message);
        kafkaTemplate.send(profileImageDeletedTopic, message);
    }
}
