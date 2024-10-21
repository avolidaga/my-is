package ru.se.ifmo.tinder.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.se.ifmo.tinder.dto.user_request.UserRequestDto;
import ru.se.ifmo.tinder.mapper.UserRequestMapper;
import ru.se.ifmo.tinder.model.User;
import ru.se.ifmo.tinder.model.UserRequest;
import ru.se.ifmo.tinder.model.UserSpacesuitData;
import ru.se.ifmo.tinder.model.enums.SearchStatus;
import ru.se.ifmo.tinder.model.enums.RequestStatus;
import ru.se.ifmo.tinder.repository.UserRepository;
import ru.se.ifmo.tinder.repository.UserRequestRepository;
import ru.se.ifmo.tinder.repository.UserSpacesuitDataRepository;
import ru.se.ifmo.tinder.service.exceptions.NoEntityWithSuchIdException;
import ru.se.ifmo.tinder.service.exceptions.UserNotFoundException;

import java.security.Principal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UserRequestService {
    private final UserRequestRepository userRequestRepository;
    private final UserSpacesuitDataRepository spacesuitDataRepository;
    private final UserRepository userRepository;

    public Page<UserRequestDto> getUsersRequestsByStatus(SearchStatus status, Pageable pageable) {
        Page<UserRequest> userRequestPage = switch (status) {
            case ALL -> userRequestRepository.findAll(pageable);
            case NEW -> userRequestRepository.findNew(pageable);
            case DECLINED -> userRequestRepository.findDeclined(pageable);
            case READY -> userRequestRepository.findReady(pageable);
            case IN_PROGRESS -> userRequestRepository.findInProgress(pageable);
        };
        return userRequestPage.map(UserRequestMapper::toUserRequestDto);
    }

    public UserRequestDto updateStatusStartRequest(Long userRequestId, RequestStatus status) {
        UserRequest userRequest = userRequestRepository.findById(userRequestId)
                .orElseThrow(() -> new NoEntityWithSuchIdException("User request", userRequestId));

        if (userRequest.getStatus() != RequestStatus.NEW) {
            throw new IllegalStateException("Incorrect status for request");
        }
        return updateStatus(userRequest, status);
    }

    public UserRequestDto updateStatusFinishRequest(Long userRequestId, RequestStatus status) {
        UserRequest userRequest = userRequestRepository.findById(userRequestId)
                .orElseThrow(() -> new NoEntityWithSuchIdException("User request", userRequestId));

        if (userRequest.getStatus() != RequestStatus.IN_PROGRESS) {
            throw new IllegalStateException("Incorrect status for request");
        }
        return updateStatus(userRequest, status);
    }

    @Transactional
    private UserRequestDto updateStatus(UserRequest userRequest, RequestStatus status) {
        userRequest.setStatus(status);
        userRequest.setUpdatedAt(LocalDateTime.now());
        UserRequest savedUserRequest = userRequestRepository.save(userRequest);

        UserSpacesuitData userSpacesuitData = spacesuitDataRepository.findById(userRequest.getUserRequestId())
                .orElseThrow(() -> new NoEntityWithSuchIdException("User spacesuit data", userRequest.getUserRequestId()));

        userSpacesuitData.setStatus(status);
        spacesuitDataRepository.save(userSpacesuitData);
        return UserRequestMapper.toUserRequestDto(savedUserRequest);
    }
}
