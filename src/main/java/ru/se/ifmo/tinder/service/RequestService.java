package ru.se.ifmo.tinder.service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.se.ifmo.tinder.model.UserRequest;
import ru.se.ifmo.tinder.model.UserSpacesuitData;
import ru.se.ifmo.tinder.model.enums.Status;
import ru.se.ifmo.tinder.repository.RequestRepository;
import ru.se.ifmo.tinder.repository.UserSpacesuitDataRepository;

@RequiredArgsConstructor
@Service
public class RequestService {
    private final RequestRepository requestRepository;
    private final UserSpacesuitDataRepository spacesuitDataRepository;

    // Пагинация для всех запросов
    public Page<UserRequest> getAllUserRequest(Pageable pageable) {
        return requestRepository.findAll(pageable);
    }

    // Пагинация для отклоненных запросов
    public Page<UserRequest> getDeclinedUserRequest(Pageable pageable) {
        return requestRepository.findDeclined(pageable);
    }

    // Пагинация для запросов со статусом "Готов"
    public Page<UserRequest> getReadyUserRequest(Pageable pageable) {
        return requestRepository.findReady(pageable);
    }

    // Пагинация для запросов "В процессе"
    public Page<UserRequest> getInProgressUserRequest(Pageable pageable) {
        return requestRepository.findInProgress(pageable);
    }

    // Обновление статуса запроса на "Готов"
    public void updateStatusReady(Integer userRequestId) {
        UserRequest userRequest = requestRepository.findById(userRequestId)
                .orElseThrow(() -> new RuntimeException("User request not found with id: " + userRequestId));

        userRequest.setStatus(Status.READY);
        requestRepository.save(userRequest);

        UserSpacesuitData userSpacesuitData = spacesuitDataRepository.findById(userRequest.getUser_request_id())
                .orElseThrow(() -> new RuntimeException("User spacesuit data not found with id: " + userRequest.getUserSpacesuitDataId()));

        userSpacesuitData.setStatus(Status.READY);
        spacesuitDataRepository.save(userSpacesuitData);
    }

    // Обновление статуса запроса на "Отклонено"
    public void updateStatusDeclined(Integer userRequestId) {
        UserRequest userRequest = requestRepository.findById(userRequestId)
                .orElseThrow(() -> new RuntimeException("User request not found with id: " + userRequestId));

        userRequest.setStatus(Status.DECLINED);
        requestRepository.save(userRequest);

        UserSpacesuitData userSpacesuitData = spacesuitDataRepository.findById(userRequest.getUser_request_id())
                .orElseThrow(() -> new RuntimeException("User spacesuit data not found with id: " + userRequest.getUserSpacesuitDataId()));

        userSpacesuitData.setStatus(Status.DECLINED);
        spacesuitDataRepository.save(userSpacesuitData);
    }
}
