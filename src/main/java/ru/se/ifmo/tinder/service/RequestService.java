package ru.se.ifmo.tinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import ru.se.ifmo.tinder.model.UserData;
import ru.se.ifmo.tinder.model.UserRequest;
import ru.se.ifmo.tinder.model.UserSpacesuitData;
import ru.se.ifmo.tinder.model.enums.Status;
import ru.se.ifmo.tinder.repository.RequestRepository;
import ru.se.ifmo.tinder.repository.UserSpacesuitDataRepository;


import java.util.List;

@RequiredArgsConstructor
@Service
public class RequestService {
    private final RequestRepository requestRepository;
    private final UserSpacesuitDataRepository spacesuitDataRepository;

    public List<UserRequest> getAllUserRequest(){
        List<Integer> idList = requestRepository.getAllUserRequestIds();
        return requestRepository.getListAllByUserRequestIdIn(idList);
    }

    public List<UserRequest> getDeclinedUserRequest(){
        List<Integer> idList = requestRepository.getDeclinedUserRequestIds();
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

    public void updateStatusReady(Integer userRequestId){
        UserRequest userRequest = requestRepository.findById(userRequestId)
                .orElseThrow(() -> new RuntimeException("User request not found with id: " + userRequestId));

        userRequest.setStatus(Status.READY);
        requestRepository.save(userRequest);

        UserSpacesuitData userSpacesuitData = spacesuitDataRepository.findById(userRequest.getUser_request_id())
                    .orElseThrow(() -> new RuntimeException("User spacesuit data not found with id: " + userRequest.getUserSpacesuitDataId()));

            userSpacesuitData.setStatus(Status.READY);
            spacesuitDataRepository.save(userSpacesuitData);

    }


    public void updateStatusDeclined(Integer userRequestId){
        UserRequest userRequest = requestRepository.findById(userRequestId)
                .orElseThrow(() -> new RuntimeException("User request not found with id: " + userRequestId));

        userRequest.setStatus(Status.DECLINED);
        requestRepository.save(userRequest);

        UserSpacesuitData userSpacesuitData = spacesuitDataRepository.findById(userRequest.getUser_request_id())
                .orElseThrow(() -> new RuntimeException("User spacesuit data not found with id: " + userRequest.getUserSpacesuitDataId()));

        userSpacesuitData.setStatus(Status.DECLINED);
        spacesuitDataRepository.save(userSpacesuitData);    }

    //public Integer insertUserData(LocalDate birthdate, Sex sex, Integer weight, Integer height, String hairColor, Location location, String firstname, Principal principal){
    //        String username = principal.getName();
    //        Optional<User> user = userRepository.findByUsername(username);
    //        Integer userId = user.get().getId();
    //        return userDataRepository.insertUserData(birthdate,sex.toString(),weight,height,hairColor, location.toString(),firstname, userId);
    //    } updateStatusDeclined
}
