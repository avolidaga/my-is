package ru.se.ifmo.tinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import ru.se.ifmo.tinder.model.UserData;
import ru.se.ifmo.tinder.model.UserRequest;
import ru.se.ifmo.tinder.model.enums.Status;
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

    public void updateStatusReady(Integer user_request_id){
        requestRepository.updateStatusReady(user_request_id);
    }

    //public Integer insertUserData(LocalDate birthdate, Sex sex, Integer weight, Integer height, String hairColor, Location location, String firstname, Principal principal){
    //        String username = principal.getName();
    //        Optional<User> user = userRepository.findByUsername(username);
    //        Integer userId = user.get().getId();
    //        return userDataRepository.insertUserData(birthdate,sex.toString(),weight,height,hairColor, location.toString(),firstname, userId);
    //    }
}
