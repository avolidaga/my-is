package ru.se.ifmo.tinder.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.se.ifmo.tinder.dto.RequestDto;
import ru.se.ifmo.tinder.model.UserRequest;
import ru.se.ifmo.tinder.model.enums.SearchStatus;
import ru.se.ifmo.tinder.model.enums.Status;
import ru.se.ifmo.tinder.service.RequestService;
import ru.se.ifmo.tinder.utils.PaginationUtil;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-request-management")
@SecurityRequirement(name = "basicAuth")
public class RequestController {
    private final RequestService requestService;

    // Получение запросов пользователей по статусу с поддержкой пагинации
    @GetMapping("user-request")
    public ResponseEntity<Page<UserRequest>> getUserRequest(
            @RequestParam Optional<SearchStatus> status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size) {

        if (status.isEmpty()) {
            return ResponseEntity.badRequest().build(); // Если статус не указан, возвращаем ошибку 400
        }

        Pageable pageable = PageRequest.of(page, size); // Создаем объект Pageable для пагинации
        Page<UserRequest> requestPage;

        // Выбираем нужный метод сервиса в зависимости от статуса
        switch (status.get()) {
            case ALL -> requestPage = requestService.getAllUserRequest(pageable);
            case DECLINED -> requestPage = requestService.getDeclinedUserRequest(pageable);
            case READY -> requestPage = requestService.getReadyUserRequest(pageable);
            case IN_PROGRESS -> requestPage = requestService.getInProgressUserRequest(pageable);
            default -> {
                return ResponseEntity.badRequest().build(); // Неправильный статус — ошибка 400
            }
        }

        // Применяем метод для создания заголовков с информацией о пагинации
        HttpHeaders headers = PaginationUtil.endlessSwipeHeadersCreate(requestPage);


        return ResponseEntity.ok().headers(headers).body(requestPage);
    }


    // Обновление статуса запроса
    @PutMapping("user-request")
    public ResponseEntity<Void> updateRequestStatus(
            @RequestParam Status status,
            @RequestBody RequestDto userRequestDto) {

        if (userRequestDto == null || userRequestDto.getUser_spacesuit_data_id() == null) {
            return ResponseEntity.badRequest().build(); // Если не переданы данные, возвращаем ошибку 400
        }

        // Обновляем статус запроса в зависимости от переданного статуса
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
