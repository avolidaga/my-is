package ru.se.ifmo.tinder.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.se.ifmo.tinder.dto.ShootingDto;
import ru.se.ifmo.tinder.service.ShootingService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
@SecurityRequirement(name = "basicAuth")
public class TestController {
    private final ShootingService shootingService;

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
}
