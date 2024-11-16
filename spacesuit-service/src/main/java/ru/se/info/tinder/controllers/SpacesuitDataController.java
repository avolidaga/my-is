package ru.se.info.tinder.controllers;

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


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/spacesuit-data")
@Slf4j
public class SpacesuitDataController {

    private final SpacesuitDataService spacesuitDataService;

    @PostMapping("new")
    public ResponseEntity<UserRequestDto> createUserSpacesuitData(@Valid @RequestBody CreateSpacesuitDataDto dto) {
        UserRequestDto userRequestDto = spacesuitDataService.createSpacesuitData(dto);
        return new ResponseEntity<>(userRequestDto, HttpStatus.CREATED);
    }

    @PutMapping("{spacesuitDataId}")
    public ResponseEntity<SpacesuitDataDto> updateSpacesuitDataById(@Valid @RequestBody UpdateSpacesuitDataDto dto) {
        SpacesuitDataDto spacesuitDataDto = spacesuitDataService.updateSpacesuitData(dto);
        return ResponseEntity.ok(spacesuitDataDto);
    }

    @DeleteMapping("{spacesuitDataId}")
    public ResponseEntity<Void> deleteSpacesuitDataById(@PathVariable Long spacesuitDataId) {
        spacesuitDataService.deleteSpacesuitData(spacesuitDataId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{spacesuitDataId}")
    public ResponseEntity<SpacesuitDataDto> getSpacesuitDataById(@PathVariable Long spacesuitDataId) {
        SpacesuitDataDto spacesuitDataDto = spacesuitDataService.getSpacesuitData(spacesuitDataId);
        return ResponseEntity.ok(spacesuitDataDto);
    }

    @GetMapping("my")
    public ResponseEntity<Page<SpacesuitDataDto>> getCurrUserSpacesuitData(@RequestParam int page,
                                                                           @RequestParam @Min(1) @Max(50) int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SpacesuitDataDto> spacesuitDataPage = spacesuitDataService.getCurrentUserSpacesuitData(pageable);
        return ResponseEntity.ok(spacesuitDataPage);
    }

    @ExceptionHandler(value = {NoSpacesuitDataException.class})
    public ResponseEntity<?> handleIncorrectRequestExceptions(RuntimeException ex) {
        log.error("Incorrect request: {}", ex.getMessage());
        return ResponseEntity.badRequest().body("Incorrect request: " + ex.getMessage());
    }
}
