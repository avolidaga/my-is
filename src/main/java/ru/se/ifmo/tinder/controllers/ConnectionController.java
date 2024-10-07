package ru.se.ifmo.tinder.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.se.ifmo.tinder.dto.UserConnectionsDto;
import ru.se.ifmo.tinder.model.User;
import ru.se.ifmo.tinder.service.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/connection-management/connections")
@SecurityRequirement(name = "basicAuth")
public class ConnectionController {
    private final UserService userService;

    // Метод для добавления соединений между пользователями с валидацией
    @PostMapping
    public ResponseEntity<Integer> connectUsers(@Valid @RequestBody UserConnectionsDto userConnectionsDto, Principal principal) {
        Integer id = userService.addConnection(principal, userConnectionsDto.getUser_data_id_2());
        return ResponseEntity.ok(id);
    }

    // Метод для получения списка соединений
    @GetMapping
    public ResponseEntity<List<User>> getConnections(Principal principal) {
        List<User> list = userService.getConnections(principal);
        return ResponseEntity.ok(list);
    }
}
