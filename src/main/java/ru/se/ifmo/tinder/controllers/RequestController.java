package ru.se.ifmo.tinder.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.se.ifmo.tinder.dto.RequestDto;
import ru.se.ifmo.tinder.model.UserRequest;
import ru.se.ifmo.tinder.model.enums.SearchStatus;
import ru.se.ifmo.tinder.model.enums.Status;
import ru.se.ifmo.tinder.service.RequestService;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-request-management")
@SecurityRequirement(name = "basicAuth")
public class RequestController {
    private final RequestService requestService;

    // Получение запросов пользователей по статусу
    @GetMapping("user-request")
    public ResponseEntity<List<UserRequest>> getUsersRequests(@NotNull @RequestParam SearchStatus status) {
        List<UserRequest> list = requestService.getUserRequestsByStatus(status);
        return ResponseEntity.ok(list);
    }

    // Обновление статуса запроса
    @PutMapping("user-request")
    public ResponseEntity<String> updateRequestStatus(@NotNull @RequestParam Status status, @Valid @RequestBody RequestDto userRequestDto) {
        switch (status) {
            case IN_PROGRESS ->
                    requestService.updateStatusStartRequest(userRequestDto.getUser_spacesuit_data_id(), status);
            case READY, DECLINED ->
                    requestService.updateStatusFinishRequest(userRequestDto.getUser_spacesuit_data_id(), status);
            default -> {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect status"); // Неправильный статус — ошибка 400
            }
        }
        return ResponseEntity.noContent().build(); // Успешное обновление статуса — код 204 (без контента)
    }
}

