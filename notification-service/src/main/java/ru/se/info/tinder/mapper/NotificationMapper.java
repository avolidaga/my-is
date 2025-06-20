package ru.se.info.tinder.mapper;

import org.springframework.stereotype.Component;
import ru.se.info.tinder.dto.MessageType;
import ru.se.info.tinder.dto.NotificationMessage;
import ru.se.info.tinder.entity.Notification;

@Component
public class NotificationMapper {
    public Notification toEntity(NotificationMessage message, MessageType type) {
        Notification entity = new Notification();
        entity.setUserId(message.getUserId());
        entity.setEmail(message.getEmail());
        entity.setContent(message.getContent());
        return entity;
    }
}
