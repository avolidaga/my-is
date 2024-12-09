package ru.se.info.tinder.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.se.info.tinder.dto.AuthUserDto;
import ru.se.info.tinder.dto.RequestUserDto;
import ru.se.info.tinder.dto.ResponseUserDto;
import ru.se.info.tinder.dto.UserDto;
import ru.se.info.tinder.mapper.UserMapper;
import ru.se.info.tinder.service.UserService;
import ru.se.info.tinder.utils.JwtTokensUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-management")
@Validated
public class UserController {
    private final UserService userService;
    private final JwtTokensUtils jwtTokensUtils;

    @PostMapping("users/new")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Mono<UserDto> createUser(@Valid @RequestBody RequestUserDto requestUserDto) {
        return userService.createUser(requestUserDto).map(UserMapper::toDtoUser);
    }

    @GetMapping("users/{userId}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public Mono<UserDto> getUserById(@NotNull @PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("users/{userId}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public Mono<UserDto> updateUserById(@NotNull @PathVariable Long userId,
                                        @Valid @RequestBody RequestUserDto requestUserDto,
                                        Principal principal) {
        return userService.updateUserById(userId, requestUserDto, principal);
    }

    @DeleteMapping("users/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public Mono<Void> deleteUserById(@NotNull @PathVariable Long userId,
                                     Principal principal) {
        return userService.deleteUserById(userId, principal);
    }

    @PostMapping("/auth-info")
    public Mono<AuthUserDto> loginUser(@Valid @RequestBody RequestUserDto requestUserDto) {
        return userService.loginUser(requestUserDto);
    }

    @PostMapping("/validation")
    public Mono<ResponseUserDto> validateToken(@RequestBody String token) {
        return jwtTokensUtils.check(token).map(jwtTokensUtils::createUserDto);
    }
}
