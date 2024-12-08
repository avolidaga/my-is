package ru.se.info.tinder.controllers;

import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public Mono<UserConnectionDto> createConnection(@Valid @RequestParam Long userDataId,
                                                    Principal principal) {
        return userConnectionService.createConnection(userDataId, principal);
    }

    @GetMapping("my")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public Flux<UserConnectionDto> getUserConnections(Principal principal) {
        return userConnectionService.getConnections(principal);
    }

    @GetMapping("my/{connectionId}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public Mono<UserConnectionDto> getUserConnectionById(@NotNull @PathVariable Long connectionId,
                                                         Principal principal) {
        return userConnectionService.getUserConnectionById(connectionId, principal);
    }
}
