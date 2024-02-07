package ru.se.ifmo.tinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.se.ifmo.tinder.model.UserData;
import ru.se.ifmo.tinder.model.UserRequest;
import ru.se.ifmo.tinder.repository.RequestRepository;


import java.util.List;

@RequiredArgsConstructor
@Service
public class RequestService {
    private final RequestRepository requestRepository;

    public List<UserRequest> getAllUserRequest(){
        List<Integer> idList = requestRepository.getAllUserRequest();
        return requestRepository.getListAllByUserRequestIdIn(idList);
    }

    public List<UserRequest> getDeclinedUserRequest(){
        List<Integer> idList = requestRepository.getDeclinedUserRequest();
        return requestRepository.getListAllByUserRequestIdIn(idList);
    }

    public List<UserRequest> getReadyUserRequest(){
        List<Integer> idList = requestRepository.getReadyUserRequest();
        return requestRepository.getListAllByUserRequestIdIn(idList);
    }

    public List<UserRequest> getInProgressUserRequest(){
        List<Integer> idList = requestRepository.getInProgressUserRequest();
        return requestRepository.getListAllByUserRequestIdIn(idList);
    }

}
