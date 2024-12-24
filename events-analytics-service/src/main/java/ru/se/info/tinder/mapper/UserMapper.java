package ru.se.info.tinder.mapper;

import org.springframework.stereotype.Component;
import ru.se.info.tinder.dto.MessageType;
import ru.se.info.tinder.dto.UserMessage;
import ru.se.info.tinder.entity.User;

@Component
public class UserMapper {
    public User toEntity(UserMessage message, MessageType type) {
        User entity = new User();
        entity.setUserId(message.getUserId());
        entity.setUsername(message.getUsername());
        entity.setType(type);
        return entity;
    }
}
