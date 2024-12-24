package ru.se.info.tinder.controllers;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
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
import ru.se.info.tinder.dto.CreateUserDataDto;
import ru.se.info.tinder.dto.UpdateUserDataDto;
import ru.se.info.tinder.dto.UserDataDto;
import ru.se.info.tinder.service.UserDataService;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-data")
@OpenAPIDefinition(
        servers = {@Server(url = "http://localhost:8080")}
)
public class UserDataController {

    private final UserDataService userDataService;

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
    public UserDataDto createUserData(@Valid @RequestBody CreateUserDataDto createUserDataDto,
                                      @RequestHeader("Authorization") String token,
                                      Principal principal) {
        return userDataService.createUserData(createUserDataDto, token, principal);
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
    public UserDataDto updateUserDataByUser(@Valid @RequestBody UpdateUserDataDto updateUserDataDto,
                                            @PathVariable Long id,
                                            Principal principal,
                                            @RequestHeader("Authorization") String token) {
        return userDataService.updateUserDataById(updateUserDataDto, id, principal, token);
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
        userDataService.deleteUserDataById(id);
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
    public UserDataDto getUserDataById(@PathVariable Long id) {
        return userDataService.getUserDataDtoById(id);
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
    public List<UserDataDto> getUsersByLocationId(@PathVariable Long locationId,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size) {
        return userDataService.getUsersDataByLocationId(locationId)
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
    public List<UserDataDto> getAllUsersData(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size) {
        return userDataService.getAllUsersData().stream()
                .skip((long) page * size)
                .limit(size).toList();
    }
}
