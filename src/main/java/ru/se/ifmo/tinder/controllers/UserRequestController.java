package ru.se.ifmo.tinder.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
import ru.se.ifmo.tinder.dto.user_request.GetUserRequestDto;
import ru.se.ifmo.tinder.dto.user_request.UserRequestDto;
import ru.se.ifmo.tinder.model.enums.SearchStatus;
import ru.se.ifmo.tinder.model.enums.RequestStatus;
import ru.se.ifmo.tinder.service.UserRequestService;
import ru.se.ifmo.tinder.utils.PaginationUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-request")
@SecurityRequirement(name = "basicAuth")
public class UserRequestController {
    private final UserRequestService userRequestService;

    @PatchMapping("{userRequestId}")
    public ResponseEntity<?> updateUserRequestStatus(@NotNull @RequestParam RequestStatus status,
                                                     @NotNull @PathVariable Long userRequestId) {
        switch (status) {
            case IN_PROGRESS -> {
                return ResponseEntity.ok(userRequestService.updateStatusStartRequest(userRequestId, status));
            }
            case READY, DECLINED -> {
                return ResponseEntity.ok(userRequestService.updateStatusFinishRequest(userRequestId, status));
            }
            default -> {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect status");
            }
        }
    }

    @GetMapping
    public ResponseEntity<Page<UserRequestDto>> getUsersRequests(@NotNull @RequestParam SearchStatus status,
                                                                 @NotNull @RequestParam int page,
                                                                 @NotNull @RequestParam @Min(1) @Max(50) int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserRequestDto> requestPage = userRequestService.getUsersRequestsByStatus(status, pageable);
        HttpHeaders headers = PaginationUtil.endlessSwipeHeadersCreate(requestPage);

        return ResponseEntity.ok().headers(headers).body(requestPage);
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
