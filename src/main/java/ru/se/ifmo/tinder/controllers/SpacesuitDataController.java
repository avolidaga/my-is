package ru.se.ifmo.tinder.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.se.ifmo.tinder.dto.spacesuit_data.CreateSpacesuitDataDto;
import ru.se.ifmo.tinder.dto.spacesuit_data.UserSpacesuitDataDto;
import ru.se.ifmo.tinder.dto.user_request.UserRequestDto;
import ru.se.ifmo.tinder.service.UserSpacesuitDataService;
import ru.se.ifmo.tinder.service.exceptions.NoSpacesuitDataException;
import ru.se.ifmo.tinder.service.exceptions.UserNotCompletedRegistrationException;

import java.security.Principal;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/spacesuit-data")
@SecurityRequirement(name = "basicAuth")
public class SpacesuitDataController {

    private final UserSpacesuitDataService userSpacesuitDataService;

    // TODO дописать возможность обновить/удалить spacesuit data
    @PostMapping
    public ResponseEntity<UserRequestDto> createSpacesuitData(@Valid @RequestBody CreateSpacesuitDataDto spacesuitDataDto,
                                                              Principal principal) {
        UserRequestDto userRequestDto = userSpacesuitDataService.createUserSpacesuitData(spacesuitDataDto, principal);
        return new ResponseEntity<>(userRequestDto, HttpStatus.CREATED);
    }

    @GetMapping("my")
    public ResponseEntity<Page<UserSpacesuitDataDto>> getCurrentUserSpacesuitData(Principal principal,
                                                                                  @RequestParam int page,
                                                                                  @RequestParam @Min(1) @Max(50) int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserSpacesuitDataDto> spacesuitDataPage = userSpacesuitDataService.getCurrentUserSpacesuitData(principal, pageable);
        return ResponseEntity.ok(spacesuitDataPage);
    }

    @ExceptionHandler(value = {UserNotCompletedRegistrationException.class, NoSpacesuitDataException.class})
    public ResponseEntity<?> handleIncorrectRequestExceptions(RuntimeException ex) {
        return ResponseEntity.badRequest().body("Incorrect request: " + ex);
    }
}
