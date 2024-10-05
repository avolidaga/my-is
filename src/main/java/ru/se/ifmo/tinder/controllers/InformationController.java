package ru.se.ifmo.tinder.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.se.ifmo.tinder.dto.ShootingDto;
import ru.se.ifmo.tinder.model.UserData;
import ru.se.ifmo.tinder.model.UserSpacesuitData;
import ru.se.ifmo.tinder.model.enums.Status;
import ru.se.ifmo.tinder.service.ShootingService;
import ru.se.ifmo.tinder.service.UserDataService;
import ru.se.ifmo.tinder.service.UserSpacesuitDataService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/info")
@SecurityRequirement(name = "basicAuth")
public class InformationController {
    private final ShootingService shootingService;
    private final UserDataService userDataService;
    private final UserSpacesuitDataService userSpacesuitDataService;

    @GetMapping("users/{planetId}")
    public ResponseEntity<List<UserData>> getUsersByPlanetId(@PathVariable String planetId){
        List<UserData> list = userDataService.getMars();
        return ResponseEntity.ok(list);
    }

    @GetMapping("users")
    public  ResponseEntity<List<UserData>> getAllUserData(Principal principal){
        List<UserData> list = userDataService.getAllUserData(principal);
        return ResponseEntity.ok(list);
    }

    @GetMapping("current-user")
    public  ResponseEntity<List<UserData>> getCurrUserData(Principal principal){
        List<UserData> list = userDataService.getCurrUserData(principal);
        return ResponseEntity.ok(list);
    }

    @GetMapping("current-user/id")
    public ResponseEntity<Long> getId(@RequestBody ShootingDto shootingDto){
        Long id = shootingService.insertShooting(shootingDto.getUsername(),shootingDto.getIsKronbars());
        return ResponseEntity.ok(id);
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
