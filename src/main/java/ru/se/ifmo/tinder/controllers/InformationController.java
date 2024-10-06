package ru.se.ifmo.tinder.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.se.ifmo.tinder.model.UserData;
import ru.se.ifmo.tinder.model.UserSpacesuitData;
import ru.se.ifmo.tinder.model.enums.Status;
import ru.se.ifmo.tinder.service.UserDataService;
import ru.se.ifmo.tinder.service.UserSpacesuitDataService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/info")
@SecurityRequirement(name = "basicAuth")
public class InformationController {
    private final UserDataService userDataService;
    private final UserSpacesuitDataService userSpacesuitDataService;

    @GetMapping("users/{planetId}")
    public ResponseEntity<List<UserData>> getUsersByPlanetId(@PathVariable String planetId){
        List<UserData> list = userDataService.getUsersByPlanetId(planetId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("users/data")
    public  ResponseEntity<List<UserData>> getAllUsersData(Principal principal){
        List<UserData> list = userDataService.getAllUsersData(principal);
        return ResponseEntity.ok(list);
    }

    @GetMapping("current-user/data")
    public  ResponseEntity<List<UserData>> getCurrUserData(Principal principal){
        Optional<UserData> list = userDataService.getCurrUserData(principal);
        if (!list.isEmpty()){
            ResponseEntity.ok(list);
        }
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("current-user/spacesuit")
    public ResponseEntity<List<Status>> getCurrUserSpacesuitData(Principal principal){
        List<UserSpacesuitData> list = userSpacesuitDataService.getCurrUserSpacesuitData(principal);
        if (!list.isEmpty()) {
            return ResponseEntity.ok(list.stream().map(UserSpacesuitData::getStatus).collect(Collectors.toList()));
        }
        return ResponseEntity.ok(List.of());
    }
}
