package ru.se.info.tinder.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.se.info.tinder.dto.CreateSpacesuitDataDto;
import ru.se.info.tinder.dto.UpdateSpacesuitDataDto;
import ru.se.info.tinder.dto.SpacesuitDataDto;
import ru.se.info.tinder.dto.UserRequestDto;
import ru.se.info.tinder.service.SpacesuitDataService;
import ru.se.info.tinder.service.exception.NoSpacesuitDataException;

import javax.validation.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/spacesuit-data")
@Slf4j
public class SpacesuitDataController {

    private final SpacesuitDataService spacesuitDataService;

    @PostMapping("new")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(security = {@SecurityRequirement(name = "bearer-key")}, summary = "Create a new spacesuit data entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created new spacesuit data"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public Mono<UserRequestDto> createUserSpacesuitData(@Valid @RequestBody CreateSpacesuitDataDto dto,
                                                        @RequestHeader("Authorization") String token) {
        return spacesuitDataService.createSpacesuitData(dto, token);
    }

    @PutMapping("{spacesuitDataId}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")}, summary = "Update an existing spacesuit data entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated spacesuit data"),
            @ApiResponse(responseCode = "400", description = "Invalid spacesuit data ID or input data"),
            @ApiResponse(responseCode = "404", description = "Spacesuit data not found")
    })
    public Mono<SpacesuitDataDto> updateSpacesuitDataById(@Valid @RequestBody UpdateSpacesuitDataDto dto,
                                                          Principal principal,
                                                          @RequestHeader("Authorization") String token) {
        return spacesuitDataService.updateSpacesuitData(dto, principal, token);
    }

    @DeleteMapping("{spacesuitDataId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(security = {@SecurityRequirement(name = "bearer-key")}, summary = "Delete a spacesuit data entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted spacesuit data"),
            @ApiResponse(responseCode = "404", description = "Spacesuit data not found")
    })
    public Mono<Object> deleteSpacesuitDataById(@PathVariable Long spacesuitDataId,
                                                Principal principal) {
        return spacesuitDataService.deleteSpacesuitData(spacesuitDataId, principal);
    }

    @GetMapping("{spacesuitDataId}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")}, summary = "Get a specific spacesuit data entry by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved spacesuit data"),
            @ApiResponse(responseCode = "404", description = "Spacesuit data not found")
    })
    public Mono<SpacesuitDataDto> getSpacesuitDataById(@PathVariable Long spacesuitDataId,
                                                       Principal principal) {
        return spacesuitDataService.getSpacesuitData(spacesuitDataId, principal);
    }

    @GetMapping("my")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")}, summary = "Get current user's spacesuit data entries")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved spacesuit data entries"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    public Flux<SpacesuitDataDto> getCurrUserSpacesuitData(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size,
                                                           Principal principal) {
        return spacesuitDataService.getCurrentUserSpacesuitData(principal)
                .skip((long) page * size)
                .take(size);
    }

    @ExceptionHandler(value = {NoSpacesuitDataException.class})
    public ResponseEntity<?> handleIncorrectRequestExceptions(RuntimeException ex) {
        log.error("Incorrect request: {}", ex.getMessage());
        return ResponseEntity.badRequest().body("Incorrect request: " + ex.getMessage());
    }
}
