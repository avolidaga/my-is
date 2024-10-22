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
import ru.se.ifmo.tinder.dto.user_data.CreateUserDataDto;
import ru.se.ifmo.tinder.dto.user_data.UpdateUserDataDto;
import ru.se.ifmo.tinder.dto.user_data.UserDataDto;
import ru.se.ifmo.tinder.service.UserDataService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-data")
@SecurityRequirement(name = "basicAuth")
public class UserDataController {

    private final UserDataService userDataService;

    @PostMapping("new")
    public ResponseEntity<UserDataDto> createUserData(@Valid @RequestBody CreateUserDataDto createUserDataDto,
                                                      Principal principal) {
        UserDataDto userDataDto = userDataService.createUserData(createUserDataDto, principal);
        return new ResponseEntity<>(userDataDto, HttpStatus.CREATED);
    }

    @PutMapping("my")
    public ResponseEntity<UserDataDto> updateUserDataByUser(@Valid @RequestBody UpdateUserDataDto updateUserDataDto,
                                                            Principal principal) {
        UserDataDto userData = userDataService.updateUserData(updateUserDataDto, principal);
        return ResponseEntity.ok(userData);
    }

    @DeleteMapping("my")
    public ResponseEntity<Void> deleteUserDataByUser(Principal principal) {
        userDataService.deleteUserDataByUser(principal);
        return ResponseEntity.ok().build();
    }

    @GetMapping("my")
    public ResponseEntity<UserDataDto> getUserDataByUser(Principal principal) {
        UserDataDto userData = userDataService.getUserDataByUser(principal);
        return ResponseEntity.ok(userData);
    }

    @GetMapping("{locationId}")
    public ResponseEntity<Page<UserDataDto>> getUsersByLocationId(@PathVariable Long locationId,
                                                                  @RequestParam int page,
                                                                  @RequestParam @Min(1) @Max(50) int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDataDto> userDataPage = userDataService.getUsersDataByLocationId(locationId, pageable);

        return new ResponseEntity<>(userDataPage, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<UserDataDto>> getAllUsersData(Principal principal,
                                                             @RequestParam int page,
                                                             @RequestParam @Min(1) @Max(50) int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDataDto> userDataPage = userDataService.getAllUsersData(principal, pageable);

        return new ResponseEntity<>(userDataPage, HttpStatus.OK);
    }
}
