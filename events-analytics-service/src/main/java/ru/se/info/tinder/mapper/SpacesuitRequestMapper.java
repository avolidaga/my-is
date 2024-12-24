package ru.se.info.tinder.mapper;

import org.springframework.stereotype.Component;
import ru.se.info.tinder.dto.MessageType;
import ru.se.info.tinder.dto.SpacesuitRequestMessage;
import ru.se.info.tinder.entity.SpacesuitRequest;

@Component
public class SpacesuitRequestMapper {
    public SpacesuitRequest toEntity(SpacesuitRequestMessage message, MessageType type) {
        SpacesuitRequest entity = new SpacesuitRequest();
        entity.setUserRequestId(message.getUserRequestId());
        entity.setSpacesuitDataId(message.getSpacesuitDataId());
        entity.setStatus(message.getStatus());
        entity.setCreatedAt(message.getCreatedAt());
        entity.setUpdatedAt(message.getUpdatedAt());
        entity.setType(type);
        return entity;
    }
}
