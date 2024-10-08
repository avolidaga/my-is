package ru.se.ifmo.tinder.controllers;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.se.ifmo.tinder.dto.LoginResponseDto;
import ru.se.ifmo.tinder.dto.UserDto;
import ru.se.ifmo.tinder.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth-management")
@Validated
public class AuthController {
    private final UserService userService;

    // Регистрация пользователя с валидацией
    @PostMapping("/register")
    public ResponseEntity<?> registrationUser(@Valid @RequestBody UserDto userDto) {
        if (userService.createUser(userDto)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().body("User already exists");
    }

    // Логин пользователя с валидацией
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginUser(@Valid @RequestBody UserDto userDto) {
        LoginResponseDto loginResponseDto = userService.login(userDto);
        return ResponseEntity.ok(loginResponseDto);
    }
}
