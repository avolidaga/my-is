package ru.se.info.tinder.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.se.info.tinder.dto.MeetingMessage;

@Component
@RequiredArgsConstructor
@Log4j2
public class NotificationProducer {

    private final KafkaTemplate<String, MeetingMessage> kafkaTemplate;

    @Value("${spring.kafka.producer.meeting-request-changed-topic}")
    private String meetingRequestChangedTopic;

    @Value("${spring.kafka.producer.meeting-request-created-topic}")
    private String meetingRequestCreatedTopic;

    public void sendNotification(MeetingMessage message) {
        log.info("Sent to Kafka {}: {}", meetingRequestChangedTopic, message);
        kafkaTemplate.send(meetingRequestChangedTopic, message);
    }

    public void sendNotification(MeetingMessage message) {
        log.info("Sent to Kafka {}: {}", meetingRequestCreatedTopic, message);
        kafkaTemplate.send(meetingRequestCreatedTopic, message);
    }
}
