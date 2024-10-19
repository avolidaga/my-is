package ru.se.ifmo.tinder.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.se.ifmo.tinder.model.UserRequest;
import ru.se.ifmo.tinder.model.UserSpacesuitData;
import ru.se.ifmo.tinder.model.enums.SearchStatus;
import ru.se.ifmo.tinder.model.enums.RequestStatus;
import ru.se.ifmo.tinder.repository.RequestRepository;
import ru.se.ifmo.tinder.repository.UserSpacesuitDataRepository;
import ru.se.ifmo.tinder.service.exceptions.NoEntityWithSuchIdException;

@RequiredArgsConstructor
@Service
public class RequestService {
    private final RequestRepository requestRepository;
    private final UserSpacesuitDataRepository spacesuitDataRepository;

    public Page<UserRequest> getUserRequestsByStatus(SearchStatus status, Pageable pageable) {
        return switch (status) {
            case ALL -> requestRepository.findAll(pageable);
            case NEW -> requestRepository.findNew(pageable);
            case DECLINED -> requestRepository.findDeclined(pageable);
            case READY -> requestRepository.findReady(pageable);
            case IN_PROGRESS -> requestRepository.findInProgress(pageable);
        };
    }

    public void updateStatusStartRequest(Integer userRequestId, RequestStatus status) {
        UserRequest userRequest = requestRepository.findById(userRequestId)
                .orElseThrow(() -> new NoEntityWithSuchIdException("User request", userRequestId));

        if (userRequest.getStatus() != RequestStatus.NEW) {
            throw new IllegalStateException("Incorrect status for request");
        }
        updateStatus(userRequest, status);
    }

    public void updateStatusFinishRequest(Integer userRequestId, RequestStatus status) {
        UserRequest userRequest = requestRepository.findById(userRequestId)
                .orElseThrow(() -> new NoEntityWithSuchIdException("User request", userRequestId));

        if (userRequest.getStatus() != RequestStatus.IN_PROGRESS) {
            throw new IllegalStateException("Incorrect status for request");
        }
        updateStatus(userRequest, status);
    }

    @Transactional
    private void updateStatus(UserRequest userRequest, RequestStatus status) {
        userRequest.setStatus(status);
        requestRepository.save(userRequest);

        UserSpacesuitData userSpacesuitData = spacesuitDataRepository.findById(userRequest.getUser_request_id())
                .orElseThrow(() -> new NoEntityWithSuchIdException("User spacesuit data", userRequest.getUser_request_id()));

        userSpacesuitData.setStatus(status);
        spacesuitDataRepository.save(userSpacesuitData);
    }
}
