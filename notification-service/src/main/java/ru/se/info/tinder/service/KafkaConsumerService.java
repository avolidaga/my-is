package ru.se.info.tinder.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Service;
import ru.se.info.tinder.dto.SpacesuitRequestMessage;


@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final ReactiveKafkaConsumerTemplate<String, SpacesuitRequestMessage> reactiveKafkaConsumerTemplate;

    @KafkaListener(topics = {"spacesuit-request-changed"}, groupId = "spacesuit-request-group")
    public void consumeSpacesuitRequestMessages(SpacesuitRequestMessage message) {
        log.info("In consumeAppUpdates()");
        log.info("Successfully consumed {}={}", SpacesuitRequestMessage.class.getSimpleName(), message);
    }
}
