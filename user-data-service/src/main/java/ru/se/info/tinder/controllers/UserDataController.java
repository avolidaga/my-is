package ru.se.info.tinder.controllers;

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
    public ResponseEntity<UserDataDto> createUserData(@Valid @RequestBody CreateUserDataDto createUserDataDto) {
        UserDataDto userDataDto = userDataService.createUserData(createUserDataDto);
        return new ResponseEntity<>(userDataDto, HttpStatus.CREATED);
    }

    @PutMapping("my")
    public ResponseEntity updateUserDataByUser(@Valid @RequestBody UpdateUserDataDto updateUserDataDto) {
        UserDataDto userData = userDataService.updateUserData(updateUserDataDto);
        return ResponseEntity.ok(userData);
    }

    @DeleteMapping("my")
    public ResponseEntity<Void> deleteUserDataByUser() {
        userDataService.deleteUserDataByUser();
        return ResponseEntity.ok().build();
    }

    @GetMapping("my")
    public ResponseEntity<UserDataDto> getUserDataByUser() {
        UserDataDto userData = userDataService.getUserDataByUser();
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
    public ResponseEntity<Page<UserDataDto>> getAllUsersData(@RequestParam int page,
                                                             @RequestParam @Min(1) @Max(50) int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDataDto> userDataPage = userDataService.getAllUsersData(pageable);

        return new ResponseEntity<>(userDataPage, HttpStatus.OK);
    }
}
