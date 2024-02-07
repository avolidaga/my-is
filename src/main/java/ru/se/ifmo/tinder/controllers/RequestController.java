package ru.se.ifmo.tinder.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.se.ifmo.tinder.dto.RequestDto;
import ru.se.ifmo.tinder.dto.UserRequestDto;
import ru.se.ifmo.tinder.model.UserData;
import ru.se.ifmo.tinder.model.UserRequest;
import ru.se.ifmo.tinder.service.RequestService;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/request")
@SecurityRequirement(name = "basicAuth")
public class RequestController {
    private final RequestService requestService;

    @PostMapping("getAllUserRequest")
    public ResponseEntity<List<UserRequest>> getAllUserRequest(){
        List<UserRequest> list = requestService.getAllUserRequest();
        return ResponseEntity.ok(list);
    }

    @PostMapping("getDeclinedUserRequest")
    public ResponseEntity<List<UserRequest>> getDeclinedUserRequest(){
        List<UserRequest> list = requestService.getDeclinedUserRequest();
        return ResponseEntity.ok(list);
    }

    @PostMapping("getReadyUserRequest")
    public ResponseEntity<List<UserRequest>> getReadyUserRequest(){
        List<UserRequest> list = requestService.getReadyUserRequest();
        return ResponseEntity.ok(list);
    }
    @PostMapping("getInProgressUserRequest")
    public ResponseEntity<List<UserRequest>> getInProgressUserRequest(){
        List<UserRequest> list = requestService.getInProgressUserRequest();
        return ResponseEntity.ok(list);
    }


    @PostMapping("updateStatus")
    public void updateStatus(@RequestBody RequestDto userRequestDto){
        requestService.updateStatusReady(userRequestDto.getUser_spacesuit_data_id());
    }



    //@PostMapping("submitForm")
    //    public ResponseEntity<Integer> submitForm(@RequestBody UserDataDto userDataDto,  Principal principal){
    //        Integer id = userDataService.insertUserData(userDataDto.getBirth_date(),userDataDto.getSex(),userDataDto.getWeight(),userDataDto.getHeight(),userDataDto.getHair_color(),userDataDto.getLocation(),userDataDto.getFirstname(), principal);
    //        return ResponseEntity.ok(id);
    //    }


}
