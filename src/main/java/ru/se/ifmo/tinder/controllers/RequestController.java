package ru.se.ifmo.tinder.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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


}
