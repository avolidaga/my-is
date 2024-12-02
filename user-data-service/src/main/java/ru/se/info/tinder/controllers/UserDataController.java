package ru.se.info.tinder.controllers;

import io.swagger.v3.oas.annotations.Operation;
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
import ru.se.info.tinder.dto.CreateUserDataDto;
import ru.se.info.tinder.dto.UpdateUserDataDto;
import ru.se.info.tinder.dto.UserDataDto;
import ru.se.info.tinder.service.UserDataService;

import java.security.Principal;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-data")
public class UserDataController {

    private final UserDataService userDataService;

    @PostMapping("new")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<UserDataDto> createUserData(@Valid @RequestBody CreateUserDataDto createUserDataDto,
                                                      @RequestHeader("Authorization") String token) {
        UserDataDto userDataDto = userDataService.createUserData(createUserDataDto, token);
        return new ResponseEntity<>(userDataDto, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<UserDataDto> updateUserDataByUser(@Valid @RequestBody UpdateUserDataDto updateUserDataDto,
                                                            @PathVariable Long id,
                                                            Principal principal,
                                                            @RequestHeader("Authorization") String token) {
        UserDataDto userData = userDataService.updateUserDataById(updateUserDataDto, id, principal, token);
        return ResponseEntity.ok(userData);
    }

    @DeleteMapping("{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<Void> deleteUserDataByUser(@PathVariable Long id) {
        userDataService.deleteUserDataById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<UserDataDto> getUserDataById(@PathVariable Long id) {
        UserDataDto userData = userDataService.getUserDataDtoById(id);
        return ResponseEntity.ok(userData);
    }

    @GetMapping("location/{locationId}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<Page<UserDataDto>> getUsersByLocationId(@PathVariable Long locationId,
                                                                  @RequestParam int page,
                                                                  @RequestParam @Min(1) @Max(50) int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDataDto> userDataPage = userDataService.getUsersDataByLocationId(locationId, pageable);

        return new ResponseEntity<>(userDataPage, HttpStatus.OK);
    }

    @GetMapping
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<Page<UserDataDto>> getAllUsersData(@RequestParam int page,
                                                             @RequestParam @Min(1) @Max(50) int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDataDto> userDataPage = userDataService.getAllUsersData(pageable);

        return new ResponseEntity<>(userDataPage, HttpStatus.OK);
    }
}
