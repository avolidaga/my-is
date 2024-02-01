package ru.se.ifmo.tinder.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.se.ifmo.tinder.dto.ShootingDto;
import ru.se.ifmo.tinder.dto.UserDataDto;
import ru.se.ifmo.tinder.model.UserData;
import ru.se.ifmo.tinder.service.ShootingService;
import ru.se.ifmo.tinder.service.UserDataService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
//@SecurityRequirement(name = "basicAuth")
public class TestController {
    private final ShootingService shootingService;
    private final UserDataService userDataService;

    @PostMapping("post")
    public ResponseEntity<String> post(){
        return ResponseEntity.ok("sjdh");
    }
    @PostMapping("get")
    public ResponseEntity<String> get(){
        return ResponseEntity.ok("sjdh");
    }

    @PostMapping("getId")
    public ResponseEntity<Long> getId(@RequestBody ShootingDto shootingDto){
        Long id = shootingService.insertShooting(shootingDto.getUsername(),shootingDto.getIsKronbars());
        return ResponseEntity.ok(id);
    }

    @PostMapping("getMars")
    public ResponseEntity<List<UserData>> getMars(){
        List<UserData> list = userDataService.getMars();
        return ResponseEntity.ok(list);
    }

    @PostMapping("getEarth")
    public ResponseEntity<List<UserDataDto>> getEarth(){
        List<UserDataDto> list = userDataService.getEarth();
        return ResponseEntity.ok(list);
    }
}
