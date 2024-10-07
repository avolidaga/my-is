package ru.se.ifmo.tinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.se.ifmo.tinder.model.UserRequest;
import ru.se.ifmo.tinder.model.UserSpacesuitData;
import ru.se.ifmo.tinder.model.enums.SearchStatus;
import ru.se.ifmo.tinder.model.enums.Status;
import ru.se.ifmo.tinder.repository.RequestRepository;
import ru.se.ifmo.tinder.repository.UserSpacesuitDataRepository;
import ru.se.ifmo.tinder.service.exceptions.NoEntityWithSuchIdException;


import java.util.List;

@RequiredArgsConstructor
@Service
public class RequestService {
    private final RequestRepository requestRepository;
    private final UserSpacesuitDataRepository spacesuitDataRepository;

    public List<UserRequest> getUserRequestsByStatus(SearchStatus status) {
        List<Integer> idList = switch (status) {
            case ALL -> requestRepository.getAllUserRequestIds();
            case DECLINED -> requestRepository.getDeclinedUserRequestIds();
            case READY -> requestRepository.getReadyUserRequest();
            case IN_PROGRESS -> requestRepository.getInProgressUserRequest();
        };
        return requestRepository.getListAllByUserRequestIdIn(idList);
    }

    public void updateStatusStartRequest(Integer userRequestId, Status status) {
        UserRequest userRequest = requestRepository.findById(userRequestId)
                .orElseThrow(() -> new NoEntityWithSuchIdException("User request", userRequestId));

        if (userRequest.getStatus() != Status.NEW) {
            throw new IllegalStateException("Incorrect status for request");
        }
        updateStatus(userRequest, status);
    }

    public void updateStatusFinishRequest(Integer userRequestId, Status status) {
        UserRequest userRequest = requestRepository.findById(userRequestId)
                .orElseThrow(() -> new NoEntityWithSuchIdException("User request", userRequestId));

        if (userRequest.getStatus() != Status.IN_PROGRESS) {
            throw new IllegalStateException("Incorrect status for request");
        }
        updateStatus(userRequest, status);
    }

    // TODO потенциальное место для транзакции
    private void updateStatus(UserRequest userRequest, Status status) {
        userRequest.setStatus(status);
        requestRepository.save(userRequest);

        UserSpacesuitData userSpacesuitData = spacesuitDataRepository.findById(userRequest.getUser_request_id())
                .orElseThrow(() -> new NoEntityWithSuchIdException("User spacesuit data", userRequest.getUser_request_id()));

        userSpacesuitData.setStatus(status);
        spacesuitDataRepository.save(userSpacesuitData);
    }

    //public Integer insertUserData(LocalDate birthdate, Sex sex, Integer weight, Integer height, String hairColor, Location location, String firstname, Principal principal){
    //        String username = principal.getName();
    //        Optional<User> user = userRepository.findByUsername(username);
    //        Integer userId = user.get().getId();
    //        return userDataRepository.insertUserData(birthdate,sex.toString(),weight,height,hairColor, location.toString(),firstname, userId);
    //    } updateStatusDeclined
}
