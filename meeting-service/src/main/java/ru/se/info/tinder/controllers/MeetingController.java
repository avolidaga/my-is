package ru.se.info.tinder.controllers;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.se.info.tinder.dto.CreateMeetingDto;
import ru.se.info.tinder.dto.UpdateMeetingDto;
import ru.se.info.tinder.dto.MeetingDto;
import ru.se.info.tinder.dto.UserRequestDto;
import ru.se.info.tinder.service.MeetingService;
import ru.se.info.tinder.service.exception.NoMeetingException;

import javax.validation.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/meeting")
@Slf4j
@OpenAPIDefinition(
        servers = {@Server(url = "http://localhost:8080")}
)
public class MeetingController {

    private final MeetingService meetingService;

    @PostMapping("new")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(security = {@SecurityRequirement(name = "bearer-key")}, summary = "Create a new spacesuit data entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created new spacesuit data"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public Mono<UserRequestDto> createUserMeeting(@Valid @RequestBody CreateMeetingDto dto,
                                                        @Parameter(hidden = true) @RequestHeader("Authorization") String token) {
        return meetingService.createMeeting(dto, token);
    }

    @PutMapping("{MeetingId}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")}, summary = "Update an existing spacesuit data entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated spacesuit data"),
            @ApiResponse(responseCode = "400", description = "Invalid spacesuit data ID or input data"),
            @ApiResponse(responseCode = "404", description = "Spacesuit data not found")
    })
    public Mono<MeetingDto> updateMeetingById(@Valid @RequestBody UpdateMeetingDto dto,
                                                    Principal principal,
                                                    @Parameter(hidden = true) @RequestHeader("Authorization") String token) {
        return meetingService.updateMeeting(dto, principal, token);
    }

    @DeleteMapping("{MeetingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(security = {@SecurityRequirement(name = "bearer-key")}, summary = "Delete a spacesuit data entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted spacesuit data"),
            @ApiResponse(responseCode = "404", description = "Spacesuit data not found")
    })
    public Mono<Object> deleteMeetingById(@PathVariable Long MeetingId,
                                                Principal principal) {
        return meetingService.deleteMeeting(MeetingId, principal);
    }

    @GetMapping("{MeetingId}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")}, summary = "Get a specific spacesuit data entry by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved spacesuit data"),
            @ApiResponse(responseCode = "404", description = "Spacesuit data not found")
    })
    public Mono<MeetingDto> getMeetingById(@PathVariable Long MeetingId,
                                                 Principal principal) {
        return meetingService.getMeeting(MeetingId, principal);
    }

    @GetMapping("my")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")}, summary = "Get current user's spacesuit data entries")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved spacesuit data entries"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    public Flux<MeetingDto> getCurrUserMeeting(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size,
                                                     Principal principal) {
        return meetingService.getCurrentUserMeeting(principal)
                .skip((long) page * size)
                .take(size);
    }

    @ExceptionHandler(value = {NoMeetingException.class})
    public ResponseEntity<?> handleIncorrectRequestExceptions(RuntimeException ex) {
        log.error("Incorrect request: {}", ex.getMessage());
        return ResponseEntity.badRequest().body("Incorrect request: " + ex.getMessage());
    }
}
