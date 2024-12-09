package ru.se.info.tinder.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.se.info.tinder.dto.UserRequestDto;
import ru.se.info.tinder.model.enums.SearchStatus;
import ru.se.info.tinder.model.enums.UpdateRequestStatus;
import ru.se.info.tinder.service.UserRequestService;

import javax.validation.constraints.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-requests")
@Slf4j
public class UserRequestController {
    private final UserRequestService userRequestService;

    @PatchMapping("{userRequestId}")
    @PreAuthorize("hasAuthority('MANAGER')")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public Mono<UserRequestDto> updateUserRequestStatus(@NotNull @RequestParam UpdateRequestStatus status,
                                                        @NotNull @PathVariable Long userRequestId) {
        return userRequestService.updateUserRequestStatus(userRequestId, status);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('MANAGER')")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public Flux<UserRequestDto> getUsersRequests(@NotNull @RequestParam SearchStatus status,
                                                 @NotNull @RequestParam int page,
                                                 @NotNull @RequestParam @Min(1) @Max(50) int size) {
        return userRequestService.getUsersRequestsByStatus(status)
                .skip((long) page * size)
                .take(size);
    }

    @ExceptionHandler(value = {IllegalStateException.class})
    public ResponseEntity<?> handleIllegalStateExceptions(RuntimeException ex) {
        log.error("Incorrect request: {}", ex.getMessage());
        return ResponseEntity.badRequest().body("Incorrect request: " + ex.getMessage());
    }

    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    public ResponseEntity<?> handleIncorrectStatusExceptions() {
        log.error("Incorrect request param 'status'");
        return ResponseEntity.badRequest().body("Incorrect request param 'status'");
    }
}
