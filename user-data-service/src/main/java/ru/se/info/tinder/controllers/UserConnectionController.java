package ru.se.info.tinder.controllers;

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
    public ResponseEntity<UserConnectionDto> createConnection(@Valid @RequestParam Long userDataId) {
        UserConnectionDto userConnectionDto = userConnectionService.createConnection(userDataId);
        return ResponseEntity.ok(userConnectionDto);
    }

    @GetMapping("my")
    public ResponseEntity<List<UserConnectionDto>> getUserConnections() {
        List<UserConnectionDto> list = userConnectionService.getConnections();
        return ResponseEntity.ok(list);
    }

    @GetMapping("my/{connectionId}")
    public ResponseEntity<UserConnectionDto> getUserConnectionById(@NotNull @PathVariable Long connectionId) {
        UserConnectionDto userConnectionById = userConnectionService.getUserConnectionById(connectionId);
        return ResponseEntity.ok(userConnectionById);
    }
}
