package ru.se.info.tinder.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.se.info.tinder.dto.MessageType;
import ru.se.info.tinder.dto.ProfileImageMessage;
import ru.se.info.tinder.dto.SpacesuitRequestMessage;
import ru.se.info.tinder.dto.UserMessage;
import ru.se.info.tinder.mapper.ProfileImageMapper;
import ru.se.info.tinder.mapper.SpacesuitRequestMapper;
import ru.se.info.tinder.mapper.UserMapper;
import ru.se.info.tinder.repository.ProfileImageRepository;
import ru.se.info.tinder.repository.SpacesuitRequestRepository;
import ru.se.info.tinder.repository.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final SpacesuitRequestRepository spacesuitRequestRepository;
    private final ProfileImageRepository profileImageRepository;
    private final UserRepository userRepository;
    private final SpacesuitRequestMapper spacesuitRequestMapper;
    private final ProfileImageMapper profileImageMapper;
    private final UserMapper userMapper;

    @KafkaListener(topics = {"spacesuit-request-changed"}, groupId = "spacesuit-request-group")
    public void consumeSpacesuitRequestChangedMessages(SpacesuitRequestMessage message) {
        log.info("Successfully consumed {}={}", SpacesuitRequestMessage.class.getSimpleName(), message);
        saveSpacesuitRequest(message,MessageType.CHANGED);
    }

    @KafkaListener(topics = {"spacesuit-request-created"}, groupId = "spacesuit-request-group")
    public void consumeSpacesuitRequestCreatedMessages(SpacesuitRequestMessage message) {
        log.info("Successfully consumed {}={}", SpacesuitRequestMessage.class.getSimpleName(), message);
        saveSpacesuitRequest(message,MessageType.CREATED);
    }

    @KafkaListener(topics = {"profile-image-created"}, groupId = "profile-image-group")
    public void consumeProfileImageCreatedMessages(ProfileImageMessage message) {
        log.info("Successfully consumed {}={}", ProfileImageMessage.class.getSimpleName(), message);
        saveProfileImage(message, MessageType.CREATED);
    }

    @KafkaListener(topics = {"profile-image-deleted"}, groupId = "profile-image-group")
    public void consumeProfileImageDeletedMessages(ProfileImageMessage message) {
        log.info("Successfully consumed {}={}", ProfileImageMessage.class.getSimpleName(), message);
        saveProfileImage(message, MessageType.DELETED);
    }

    @KafkaListener(topics = {"user-created"}, groupId = "user-group")
    public void consumeUserCreatedMessages(UserMessage message) {
        log.info("Successfully consumed {}={}", UserMessage.class.getSimpleName(), message);
        saveUser(message, MessageType.CREATED);
    }

    @KafkaListener(topics = {"user-deleted"}, groupId = "user-group")
    public void consumeUserDeletedMessages(UserMessage message) {
        log.info("Successfully consumed {}={}", UserMessage.class.getSimpleName(), message);
        saveUser(message,MessageType.DELETED);
    }

    private void saveSpacesuitRequest(SpacesuitRequestMessage message, MessageType type) {
        var entity = spacesuitRequestMapper.toEntity(message, type);
        spacesuitRequestRepository.save(entity);
        log.info("Saved SpacesuitRequest: {}", entity);
    }

    private void saveProfileImage(ProfileImageMessage message, MessageType type) {
        var entity = profileImageMapper.toEntity(message, type);
        profileImageRepository.save(entity);
        log.info("Saved ProfileImage: {}", entity);
    }

    private void saveUser(UserMessage message, MessageType type) {
        var entity = userMapper.toEntity(message, type);
        userRepository.save(entity);
        log.info("Saved User: {}", entity);
    }
}
