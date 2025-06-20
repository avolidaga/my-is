package ru.se.info.tinder.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.se.info.tinder.dto.MessageType;
import ru.se.info.tinder.dto.NotificationMessage;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final SendNotificationService sendNotificationService;


    @KafkaListener(topics = {"notification.immediate"}, groupId = "notification-group")
    public void consumeSpacesuitRequestChangedMessages(NotificationMessage message) {
        log.info("Successfully consumed {}={}", NotificationMessage.class.getSimpleName(), message);
        sendNotificationService.saveNotification(message);
    }

    @KafkaListener(topics = {"notification.scheduled"}, groupId = "notification-group")
    public void consumeSpacesuitRequestCreatedMessages(NotificationMessage message) {
        log.info("Successfully consumed {}={}", NotificationMessage.class.getSimpleName(), message);
        sendNotificationService.sendNotification(message);
    }
}
