package ru.se.ifmo.tinder.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.se.ifmo.tinder.dto.ShootingDto;
import ru.se.ifmo.tinder.model.UserData;
import ru.se.ifmo.tinder.service.ShootingService;
import ru.se.ifmo.tinder.service.UserDataService;
import ru.se.ifmo.tinder.service.UserService;

import java.security.Principal;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/connection")
@SecurityRequirement(name = "basicAuth")
public class ConnectionController {
    private final UserDataService userDataService;
    private final UserService userService;

    @PostMapping("connectUsers")
    public ResponseEntity<String> connectUsers(@RequestBody Integer userId2, Principal principal){
        userService.addConnection(principal, userId2);
        return ResponseEntity.ok("николаев");
    }


}
