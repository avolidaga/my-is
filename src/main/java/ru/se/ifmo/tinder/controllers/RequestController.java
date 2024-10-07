package ru.se.ifmo.tinder.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-request-management")
@SecurityRequirement(name = "basicAuth")
public class RequestController {
    private final RequestService requestService;

    // Получение запросов пользователей по статусу
    @GetMapping("user-request")
    public ResponseEntity<List<UserRequest>> getUserRequest(@RequestParam Optional<SearchStatus> status) {
        if (status.isEmpty()) {
            return ResponseEntity.badRequest().build(); // Если статус не указан, возвращаем ошибку 400
        }

        List<UserRequest> list = switch (status.get()) {
            case ALL -> requestService.getAllUserRequest();
            case DECLINED -> requestService.getDeclinedUserRequest();
            case READY -> requestService.getReadyUserRequest();
            case IN_PROGRESS -> requestService.getInProgressUserRequest();
        };

        return ResponseEntity.ok(list);
    }

    // Обновление статуса запроса
    @PutMapping("user-request")
    public ResponseEntity<Void> updateRequestStatus(@RequestParam Status status, @RequestBody RequestDto userRequestDto) {
        if (userRequestDto == null || userRequestDto.getUser_spacesuit_data_id() == null) {
            return ResponseEntity.badRequest().build(); // Если не переданы данные, возвращаем ошибку 400
        }

        switch (status) {
            case READY -> requestService.updateStatusReady(userRequestDto.getUser_spacesuit_data_id());
            case DECLINED -> requestService.updateStatusDeclined(userRequestDto.getUser_spacesuit_data_id());
            default -> {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Неправильный статус — ошибка 400
            }
        }

        return ResponseEntity.noContent().build(); // Успешное обновление статуса — код 204 (без контента)
    }
}

