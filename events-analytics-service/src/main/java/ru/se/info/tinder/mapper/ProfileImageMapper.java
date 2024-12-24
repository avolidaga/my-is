package ru.se.info.tinder.mapper;

import org.springframework.stereotype.Component;
import ru.se.info.tinder.dto.MessageType;
import ru.se.info.tinder.dto.ProfileImageMessage;
import ru.se.info.tinder.entity.ProfileImage;

@Component
public class ProfileImageMapper {
    public ProfileImage toEntity(ProfileImageMessage message, MessageType type) {
        ProfileImage entity = new ProfileImage();
        entity.setImageId(message.getImageId());
        entity.setUserId(message.getUserId());
        entity.setType(type);
        return entity;
    }
}
