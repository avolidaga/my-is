package ru.se.info.tinder.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.se.info.tinder.dto.ProfileImageMessage;
import ru.se.info.tinder.dto.SpacesuitRequestMessage;
import ru.se.info.tinder.dto.UserMessage;


@Service
@Slf4j
public class KafkaConsumerService {
    @KafkaListener(topics = {"spacesuit-request-changed"}, groupId = "spacesuit-request-group")
    public void consumeSpacesuitRequestChangedMessages(SpacesuitRequestMessage message) {
        log.info("Successfully consumed {}={}", SpacesuitRequestMessage.class.getSimpleName(), message);
    }

    @KafkaListener(topics = {"spacesuit-request-created"}, groupId = "spacesuit-request-group")
    public void consumeSpacesuitRequestCreatedMessages(SpacesuitRequestMessage message) {
        log.info("Successfully consumed {}={}", SpacesuitRequestMessage.class.getSimpleName(), message);
    }

    @KafkaListener(topics = {"profile-image-created"}, groupId = "profile-image-group")
    public void consumeProfileImageCreatedMessages(ProfileImageMessage message) {
        log.info("Successfully consumed {}={}", ProfileImageMessage.class.getSimpleName(), message);
    }

    @KafkaListener(topics = {"profile-image-deleted"}, groupId = "profile-image-group")
    public void consumeProfileImageDeletedMessages(ProfileImageMessage message) {
        log.info("Successfully consumed {}={}", ProfileImageMessage.class.getSimpleName(), message);
    }

    @KafkaListener(topics = {"user-created"}, groupId = "user-group")
    public void consumeUserCreatedMessages(UserMessage message) {
        log.info("Successfully consumed {}={}", UserMessage.class.getSimpleName(), message);
    }

    @KafkaListener(topics = {"user-deleted"}, groupId = "user-group")
    public void consumeUserDeletedMessages(UserMessage message) {
        log.info("Successfully consumed {}={}", UserMessage.class.getSimpleName(), message);
    }
}
