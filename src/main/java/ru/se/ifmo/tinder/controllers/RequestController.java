package ru.se.ifmo.tinder.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.se.ifmo.tinder.dto.RequestDto;
import ru.se.ifmo.tinder.model.UserRequest;
import ru.se.ifmo.tinder.service.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-request-management")
@SecurityRequirement(name = "basicAuth")
public class RequestController {
    private final RequestService requestService;

    //TODO создать enum для status и подправить обработку
    @GetMapping("user-request/{status}")
    public ResponseEntity<List<UserRequest>> getUserRequest(@PathVariable String status) {
        List<UserRequest> list = switch (status) {
            case ("ALL") -> requestService.getAllUserRequest();
            case ("DECLINED") -> requestService.getDeclinedUserRequest();
            case ("READY") -> requestService.getReadyUserRequest();
            case ("IN_PROGRESS") -> requestService.getInProgressUserRequest();
            default -> null;
        };
        return ResponseEntity.ok(list);
    }

    @PutMapping("user-request")
    public void updateRequestStatus(@RequestParam String status, @RequestBody RequestDto userRequestDto) {
        switch (status) {
            case ("READY") -> requestService.updateStatusReady(userRequestDto.getUser_spacesuit_data_id());
            case ("DECLINED") -> requestService.updateStatusDeclined(userRequestDto.getUser_spacesuit_data_id());
            default -> { // TODO some error
            }
        }
    }
}
