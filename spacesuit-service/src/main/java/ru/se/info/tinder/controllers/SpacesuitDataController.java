package ru.se.info.tinder.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.se.info.tinder.dto.CreateSpacesuitDataDto;
import ru.se.info.tinder.dto.UpdateSpacesuitDataDto;
import ru.se.info.tinder.dto.SpacesuitDataDto;
import ru.se.info.tinder.dto.UserRequestDto;
import ru.se.info.tinder.service.SpacesuitDataService;
import ru.se.info.tinder.service.exception.NoSpacesuitDataException;

import java.security.Principal;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/spacesuit-data")
@Slf4j
public class SpacesuitDataController {

    private final SpacesuitDataService spacesuitDataService;

    @PostMapping("new")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<UserRequestDto> createUserSpacesuitData(@Valid @RequestBody CreateSpacesuitDataDto dto,
                                                                  @RequestHeader("Authorization") String token) {
        UserRequestDto userRequestDto = spacesuitDataService.createSpacesuitData(dto, token);
        return new ResponseEntity<>(userRequestDto, HttpStatus.CREATED);
    }

    @PutMapping("{spacesuitDataId}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<SpacesuitDataDto> updateSpacesuitDataById(@Valid @RequestBody UpdateSpacesuitDataDto dto,
                                                                    Principal principal,
                                                                    @RequestHeader("Authorization") String token) {
        SpacesuitDataDto spacesuitDataDto = spacesuitDataService.updateSpacesuitData(dto, principal, token);
        return ResponseEntity.ok(spacesuitDataDto);
    }

    @DeleteMapping("{spacesuitDataId}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<Void> deleteSpacesuitDataById(@PathVariable Long spacesuitDataId,
                                                        Principal principal) {
        spacesuitDataService.deleteSpacesuitData(spacesuitDataId, principal);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{spacesuitDataId}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<SpacesuitDataDto> getSpacesuitDataById(@PathVariable Long spacesuitDataId,
                                                                 Principal principal) {
        SpacesuitDataDto spacesuitDataDto = spacesuitDataService.getSpacesuitData(spacesuitDataId, principal);
        return ResponseEntity.ok(spacesuitDataDto);
    }

    @GetMapping("my")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<Page<SpacesuitDataDto>> getCurrUserSpacesuitData(@RequestParam int page,
                                                                           @RequestParam @Min(1) @Max(50) int size,
                                                                           Principal principal) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SpacesuitDataDto> spacesuitDataPage = spacesuitDataService.getCurrentUserSpacesuitData(pageable, principal);
        return ResponseEntity.ok(spacesuitDataPage);
    }

    @ExceptionHandler(value = {NoSpacesuitDataException.class})
    public ResponseEntity<?> handleIncorrectRequestExceptions(RuntimeException ex) {
        log.error("Incorrect request: {}", ex.getMessage());
        return ResponseEntity.badRequest().body("Incorrect request: " + ex.getMessage());
    }
}
