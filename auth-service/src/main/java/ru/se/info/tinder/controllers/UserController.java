package ru.se.info.tinder.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.se.ifmo.tinder.dto.user.AuthUserDto;
import ru.se.ifmo.tinder.dto.user.RequestUserDto;
import ru.se.ifmo.tinder.dto.user.UserDto;
import ru.se.ifmo.tinder.service.UserService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-management")
@Validated
public class UserController {
    private final UserService userService;

    @PostMapping("users/new")
    public ResponseEntity<Void> createUser(@Valid @RequestBody RequestUserDto requestUserDto) {
        userService.createUser(requestUserDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("users/{userId}")
    public ResponseEntity<UserDto> getUserById(@NotNull @PathVariable Long userId) {
        UserDto userDto = userService.getUserById(userId);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("users/{userId}")
    public ResponseEntity<UserDto> updateUserById(@NotNull @PathVariable Long userId,
                                                  @Valid @RequestBody RequestUserDto requestUserDto,
                                                  Principal principal) {
        UserDto userDto = userService.updateUserById(userId, requestUserDto, principal);
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("users/{userId}")
    public ResponseEntity<Void> deleteUserById(@NotNull @PathVariable Long userId,
                                               Principal principal) {
        userService.deleteUserById(userId, principal);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth-info")
    public ResponseEntity<AuthUserDto> loginUser(@Valid @RequestBody RequestUserDto requestUserDto) {
        AuthUserDto AuthUserDto = userService.loginUser(requestUserDto);
        return ResponseEntity.ok(AuthUserDto);
    }

}
