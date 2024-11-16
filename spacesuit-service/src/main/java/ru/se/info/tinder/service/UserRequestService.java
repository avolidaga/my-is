package ru.se.info.tinder.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.se.info.tinder.dto.UserRequestDto;
import ru.se.info.tinder.mapper.UserRequestMapper;
import ru.se.info.tinder.model.UserRequest;
import ru.se.info.tinder.model.SpacesuitData;
import ru.se.info.tinder.model.enums.SearchStatus;
import ru.se.info.tinder.model.enums.RequestStatus;
import ru.se.info.tinder.model.enums.UpdateRequestStatus;
import ru.se.info.tinder.repository.UserRequestRepository;
import ru.se.info.tinder.service.exception.NoEntityWithSuchIdException;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UserRequestService {
    private final UserRequestRepository userRequestRepository;

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

    public UserRequestDto updateUserRequestStatus(Long userRequestId, UpdateRequestStatus status) {
        UserRequest userRequest = userRequestRepository.findById(userRequestId)
                .orElseThrow(() -> new NoEntityWithSuchIdException("User request", userRequestId));

        switch (status) {
            case IN_PROGRESS -> {
                if (userRequest.getStatus() != RequestStatus.NEW) {
                    throw new IllegalStateException("Incorrect status for request");
                }
            }
            case READY, DECLINED -> {
                if (userRequest.getStatus() != RequestStatus.IN_PROGRESS) {
                    throw new IllegalStateException("Incorrect status for request");
                }
            }
        }
        return updateStatus(userRequest, RequestStatus.valueOf(status.name()));
    }

    @Transactional
    private UserRequestDto updateStatus(UserRequest userRequest, RequestStatus status) {
        userRequest.setStatus(status);
        userRequest.setUpdatedAt(LocalDateTime.now());
        UserRequest savedUserRequest = userRequestRepository.save(userRequest);

        return UserRequestMapper.toUserRequestDto(savedUserRequest);
    }

    public UserRequest createUserRequest(SpacesuitData spacesuitData) {
        UserRequest userRequest = UserRequest.builder()
                .spacesuitData(spacesuitData)
                .status(RequestStatus.NEW)
                .createdAt(LocalDateTime.now())
                .build();

        return userRequestRepository.save(userRequest);
    }
}
