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
import ru.se.ifmo.tinder.dto.spacesuit_data.UpdateSpacesuitDataDto;
import ru.se.ifmo.tinder.dto.spacesuit_data.SpacesuitDataDto;
import ru.se.ifmo.tinder.dto.user_request.UserRequestDto;
import ru.se.ifmo.tinder.service.SpacesuitDataService;
import ru.se.ifmo.tinder.service.exceptions.NoSpacesuitDataException;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/spacesuit-data")
@SecurityRequirement(name = "basicAuth")
public class SpacesuitDataController {

    private final SpacesuitDataService spacesuitDataService;

    @PostMapping("new")
    public ResponseEntity<UserRequestDto> createUserSpacesuitData(@Valid @RequestBody CreateSpacesuitDataDto dto,
                                                                  Principal principal) {
        UserRequestDto userRequestDto = spacesuitDataService.createSpacesuitData(dto, principal);
        return new ResponseEntity<>(userRequestDto, HttpStatus.CREATED);
    }

    @PutMapping("{spacesuitDataId}")
    public ResponseEntity<SpacesuitDataDto> updateSpacesuitDataById(@Valid @RequestBody UpdateSpacesuitDataDto dto,
                                                                    Principal principal) {
        SpacesuitDataDto spacesuitDataDto = spacesuitDataService.updateSpacesuitData(dto, principal);
        return ResponseEntity.ok(spacesuitDataDto);
    }

    @DeleteMapping("{spacesuitDataId}")
    public ResponseEntity<Void> deleteSpacesuitDataById(@PathVariable Long spacesuitDataId,
                                                        Principal principal) {
        spacesuitDataService.deleteSpacesuitData(spacesuitDataId, principal);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{spacesuitDataId}")
    public ResponseEntity<SpacesuitDataDto> getSpacesuitDataById(@PathVariable Long spacesuitDataId,
                                                                 Principal principal) {
        SpacesuitDataDto spacesuitDataDto = spacesuitDataService.getSpacesuitData(spacesuitDataId, principal);
        return ResponseEntity.ok(spacesuitDataDto);
    }

    @GetMapping("my")
    public ResponseEntity<Page<SpacesuitDataDto>> getCurrUserSpacesuitData(Principal principal,
                                                                           @RequestParam int page,
                                                                           @RequestParam @Min(1) @Max(50) int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SpacesuitDataDto> spacesuitDataPage = spacesuitDataService.getCurrentUserSpacesuitData(principal, pageable);
        return ResponseEntity.ok(spacesuitDataPage);
    }

    @ExceptionHandler(value = {NoSpacesuitDataException.class})
    public ResponseEntity<?> handleIncorrectRequestExceptions(RuntimeException ex) {
        return ResponseEntity.badRequest().body("Incorrect request: " + ex.getMessage());
    }
}
