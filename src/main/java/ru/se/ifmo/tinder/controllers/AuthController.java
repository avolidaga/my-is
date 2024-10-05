package ru.se.ifmo.tinder.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.se.ifmo.tinder.dto.LoginResponseDto;
import ru.se.ifmo.tinder.dto.UserDto;
import ru.se.ifmo.tinder.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth-management")
public class AuthController {
    private final UserService userService;

    @PostMapping("register")
    public ResponseEntity<?> registrationUser(@RequestBody UserDto userDto) {
        if (userService.createUser(userDto)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponseDto> loginUser(@RequestBody UserDto userDto) {
        userService.login(userDto);
        LoginResponseDto loginResponseDto = userService.login(userDto);
        return ResponseEntity.ok(loginResponseDto);
    }
}
