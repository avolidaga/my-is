package ru.se.info.tinder.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.se.info.tinder.dto.CreateUserDataDto;
import ru.se.info.tinder.dto.UpdateUserDataDto;
import ru.se.info.tinder.dto.UserDataDto;
import ru.se.info.tinder.service.UserDataService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.security.Principal;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-data")
public class UserDataController {

    private final UserDataService userDataService;

    @PostMapping("new")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public Mono<UserDataDto> createUserData(@Valid @RequestBody CreateUserDataDto createUserDataDto,
                                            @RequestHeader("Authorization") String token,
                                            Principal principal) {
        return userDataService.createUserData(createUserDataDto, token, principal);
    }

    @PutMapping("{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public Mono<UserDataDto> updateUserDataByUser(@Valid @RequestBody UpdateUserDataDto updateUserDataDto,
                                                  @PathVariable Long id,
                                                  Principal principal,
                                                  @RequestHeader("Authorization") String token) {
        return userDataService.updateUserDataById(updateUserDataDto, id, principal, token);
    }

    @DeleteMapping("{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public Mono<Object> deleteUserDataByUser(@PathVariable Long id) {
        return userDataService.deleteUserDataById(id);
    }

    @GetMapping("{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public Mono<UserDataDto> getUserDataById(@PathVariable Long id) {
        return userDataService.getUserDataDtoById(id);
    }

    @GetMapping("location/{locationId}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public Flux<UserDataDto> getUsersByLocationId(@PathVariable Long locationId,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size) {
        return userDataService.getUsersDataByLocationId(locationId)
                .skip((long) page * size)
                .take(size);
    }

    @GetMapping
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public Flux<UserDataDto> getAllUsersData(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size) {
        return userDataService.getAllUsersData()
                .skip((long) page * size)
                .take(size);
    }
}
