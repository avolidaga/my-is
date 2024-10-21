package ru.se.ifmo.tinder.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.se.ifmo.tinder.dto.user.AuthUserDto;
import ru.se.ifmo.tinder.dto.user.CreateUserDto;
import ru.se.ifmo.tinder.dto.user.LoginUserDto;
import ru.se.ifmo.tinder.dto.user.UserDto;
import ru.se.ifmo.tinder.model.User;
import ru.se.ifmo.tinder.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth-management")
@Validated
public class UserController {
    private final UserService userService;

    // TODO дописать методы изменения/удаления пользователя
    @PostMapping("/register")
    public ResponseEntity<UserDto> registrationUser(@Valid @RequestBody CreateUserDto createUserDto) {
        UserDto userDto = userService.createUser(createUserDto);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping("/loginUser")
    public ResponseEntity<AuthUserDto> loginUser(@Valid @RequestBody LoginUserDto loginUserDto) {
        AuthUserDto AuthUserDto = userService.loginUser(loginUserDto);
        return ResponseEntity.ok(AuthUserDto);
    }
}
