package ru.se.info.tinder.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.se.info.tinder.dto.AuthUserDto;
import ru.se.info.tinder.dto.RequestUserDto;
import ru.se.info.tinder.dto.UserDto;
import ru.se.info.tinder.service.UserService;
import ru.se.info.tinder.utils.JwtTokensUtils;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-management")
@Validated
public class UserController {
    private final UserService userService;
    private final JwtTokensUtils jwtTokensUtils;

    @PostMapping("users/new")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> createUser(@Valid @RequestBody RequestUserDto requestUserDto) {
        userService.createUser(requestUserDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("users/{userId}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<UserDto> getUserById(@NotNull @PathVariable Long userId) {
        UserDto userDto = userService.getUserById(userId);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("users/{userId}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<UserDto> updateUserById(@NotNull @PathVariable Long userId,
                                                  @Valid @RequestBody RequestUserDto requestUserDto,
                                                  Principal principal) {
        UserDto userDto = userService.updateUserById(userId, requestUserDto, principal);
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
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

    @PostMapping("/validation")
    public ResponseEntity<Boolean> validateToken(@RequestBody String token) {
        return ResponseEntity.ok(jwtTokensUtils.validateToken(token));
    }

}
