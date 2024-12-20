package ru.se.info.tinder.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(
            summary = "Create a new user",
            description = "This endpoint allows an admin to create a new user.",
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public Mono<UserDto> createUser(@Valid @RequestBody RequestUserDto requestUserDto) {
        return userService.createUser(requestUserDto).map(UserMapper::toDtoUser);
    }

    @GetMapping("users/{userId}")
    @Operation(
            summary = "Get user by ID",
            description = "This endpoint retrieves a user by their ID.",
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public Mono<UserDto> getUserById(@NotNull @PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("users/{userId}")
    @Operation(
            summary = "Update user by ID",
            description = "This endpoint allows the admin or the user themselves to update user details.",
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public Mono<UserDto> updateUserById(@NotNull @PathVariable Long userId,
                                        @Valid @RequestBody RequestUserDto requestUserDto,
                                        Principal principal) {
        return userService.updateUserById(userId, requestUserDto, principal);
    }

    @DeleteMapping("users/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Delete user by ID",
            description = "This endpoint allows an admin to delete a user by their ID.",
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User successfully deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public Mono<Void> deleteUserById(@NotNull @PathVariable Long userId,
                                     Principal principal) {
        return userService.deleteUserById(userId, principal);
    }

    @PostMapping("/auth-info")
    @Operation(
            summary = "Login user",
            description = "This endpoint allows users to log in with their credentials and obtain authentication info.",
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully logged in"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public Mono<AuthUserDto> loginUser(@Valid @RequestBody RequestUserDto requestUserDto) {
        return userService.loginUser(requestUserDto);
    }

    @PostMapping("/validation")
    @Operation(
            summary = "Validate token",
            description = "This endpoint validates a JWT token and returns user information if valid.",
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token is valid"),
            @ApiResponse(responseCode = "401", description = "Invalid or expired token")
    })
    public Mono<ResponseUserDto> validateToken(@RequestBody String token) {
        return jwtTokensUtils.check(token).map(jwtTokensUtils::createUserDto);
    }
}
