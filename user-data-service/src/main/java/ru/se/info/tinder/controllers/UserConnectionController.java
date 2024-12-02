package ru.se.info.tinder.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.se.info.tinder.dto.UserConnectionDto;
import ru.se.info.tinder.service.UserConnectionService;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-connections")
@SecurityRequirement(name = "basicAuth")
public class UserConnectionController {
    private final UserConnectionService userConnectionService;

    @PostMapping("new")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<UserConnectionDto> createConnection(@Valid @RequestParam Long userDataId,
                                                              Principal principal) {
        UserConnectionDto userConnectionDto = userConnectionService.createConnection(userDataId, principal);
        return ResponseEntity.ok(userConnectionDto);
    }

    @GetMapping("my")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<List<UserConnectionDto>> getUserConnections(Principal principal) {
        List<UserConnectionDto> list = userConnectionService.getConnections(principal);
        return ResponseEntity.ok(list);
    }

    @GetMapping("my/{connectionId}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<UserConnectionDto> getUserConnectionById(@NotNull @PathVariable Long connectionId,
                                                                   Principal principal) {
        UserConnectionDto userConnectionById = userConnectionService.getUserConnectionById(connectionId, principal);
        return ResponseEntity.ok(userConnectionById);
    }
}
