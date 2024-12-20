package ru.se.info.tinder.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.se.info.tinder.dto.UserConnectionDto;
import ru.se.info.tinder.service.UserConnectionService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-connections")
@SecurityRequirement(name = "basicAuth")
public class UserConnectionController {
    private final UserConnectionService userConnectionService;

    @PostMapping("new")
    @Operation(
            summary = "Create a new user connection",
            description = "Creates a new connection between the current user and the specified user by their user data ID.",
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Connection created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    public Mono<UserConnectionDto> createConnection(@Valid @RequestParam Long userDataId,
                                                    Principal principal) {
        return userConnectionService.createConnection(userDataId, principal);
    }

    @GetMapping("my")
    @Operation(
            summary = "Get user connections",
            description = "Fetches all connections associated with the current user.",
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Connections fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    public Flux<UserConnectionDto> getUserConnections(Principal principal) {
        return userConnectionService.getConnections(principal);
    }

    @GetMapping("my/{connectionId}")
    @Operation(
            summary = "Get a user connection by ID",
            description = "Fetches details of a specific connection by its ID for the current user.",
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Connection fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "404", description = "Connection not found")
    })
    public Mono<UserConnectionDto> getUserConnectionById(@NotNull @PathVariable Long connectionId,
                                                         Principal principal) {
        return userConnectionService.getUserConnectionById(connectionId, principal);
    }
}
