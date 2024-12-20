package ru.se.info.tinder.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.se.info.tinder.dto.SpacesuitRequestMessage;


@Service
@Slf4j
public class KafkaConsumerService {

    @KafkaListener(topics = {"spacesuit-request-changed"}, groupId = "spacesuit-request-group")
    public void consumeSpacesuitRequestMessages(SpacesuitRequestMessage message) {
        log.info("Successfully consumed {}={}", SpacesuitRequestMessage.class.getSimpleName(), message);
    }
}
