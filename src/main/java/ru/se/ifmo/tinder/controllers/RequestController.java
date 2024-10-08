package ru.se.ifmo.tinder.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.se.ifmo.tinder.dto.RequestDto;
import ru.se.ifmo.tinder.model.UserRequest;
import ru.se.ifmo.tinder.model.enums.SearchStatus;
import ru.se.ifmo.tinder.model.enums.Status;
import ru.se.ifmo.tinder.service.RequestService;
import ru.se.ifmo.tinder.utils.PaginationUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-request-management")
@SecurityRequirement(name = "basicAuth")
public class RequestController {
    private final RequestService requestService;

    // Получение запросов пользователей по статусу с поддержкой пагинации
    @GetMapping("user-request")
    public ResponseEntity<Page<UserRequest>> getUsersRequests(@NotNull @RequestParam SearchStatus status,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size) {
        Pageable pageable = PageRequest.of(page, size); // Создаем объект Pageable для пагинации
        Page<UserRequest> requestPage = requestService.getUserRequestsByStatus(status, pageable);

        // Применяем метод для создания заголовков с информацией о пагинации
        HttpHeaders headers = PaginationUtil.endlessSwipeHeadersCreate(requestPage);

        return ResponseEntity.ok().headers(headers).body(requestPage);
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

    @ExceptionHandler(value = {IllegalStateException.class})
    public ResponseEntity<?> handleIllegalStateExceptions(RuntimeException ex) {
        return ResponseEntity.badRequest().body("Incorrect request: " + ex);
    }

    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    public ResponseEntity<?> handleIncorrectStatusExceptions() {
        return ResponseEntity.badRequest().body("Incorrect request param 'status'");
    }
}
