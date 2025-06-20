package ru.se.info.tinder.controllers;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.se.info.tinder.dto.CreateUserDto;
import ru.se.info.tinder.dto.UpdateUserDto;
import ru.se.info.tinder.dto.UserDto;
import ru.se.info.tinder.service.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-data")
@OpenAPIDefinition(
        servers = {@Server(url = "http://localhost:8080")}
)
public class UserController {

    private final UserService userService;

    @PostMapping("new")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new user data",
            description = "Creates a new user data entry using the provided details.",
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User data created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    public UserDto createUserData(@Valid @RequestBody CreateUserDto createUserDto,
                                  @Parameter(hidden = true) @RequestHeader("Authorization") String token,
                                  Principal principal) {
        return userService.createUserData(createUserDto, token, principal);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "Update user data by ID",
            description = "Updates an existing user data entry by its ID.",
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User data updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "404", description = "User data not found")
    })
    public UserDto updateUserDataByUser(@Valid @RequestBody UpdateUserDto updateUserDto,
                                        @PathVariable Long id,
                                        Principal principal,
                                        @Parameter(hidden = true) @RequestHeader("Authorization") String token) {
        return userService.updateUserDataById(updateUserDto, id, principal, token);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "Delete user data by ID",
            description = "Deletes an existing user data entry by its ID.",
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User data deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "404", description = "User data not found")
    })
    public ResponseEntity<Void> deleteUserDataByUser(@PathVariable Long id) {
        userService.deleteUserDataById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{id}")
    @Operation(
            summary = "Get user data by ID",
            description = "Fetches user data details by its ID.",
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User data fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "404", description = "User data not found")
    })
    public UserDto getUserDataById(@PathVariable Long id) {
        return userService.getUserDataDtoById(id);
    }

    @GetMapping("location/{locationId}")
    @Operation(
            summary = "Get users by location ID",
            description = "Fetches user data for users in a specific location.",
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User data fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "404", description = "Location not found")
    })
    public List<UserDto> getUsersByLocationId(@PathVariable Long locationId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size) {
        return userService.getUsersDataByLocationId(locationId)
                .stream()
                .skip((long) page * size)
                .limit(size).toList();
    }

    @GetMapping
    @Operation(
            summary = "Get all user data",
            description = "Fetches all user data with pagination.",
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User data fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    public List<UserDto> getAllUsersData(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size) {
        return userService.getAllUsersData().stream()
                .skip((long) page * size)
                .limit(size).toList();
    }
}
