package ru.se.ifmo.tinder.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.se.ifmo.tinder.model.UserData;
import ru.se.ifmo.tinder.model.UserSpacesuitData;
import ru.se.ifmo.tinder.model.enums.Status;
import ru.se.ifmo.tinder.service.UserDataService;
import ru.se.ifmo.tinder.service.UserSpacesuitDataService;
import ru.se.ifmo.tinder.service.exceptions.NoSpacesuitDataException;
import ru.se.ifmo.tinder.service.exceptions.UserNotCompletedRegistrationException;
import ru.se.ifmo.tinder.utils.PaginationUtil;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/info")
@SecurityRequirement(name = "basicAuth")
public class InformationController {
    private final UserDataService userDataService;
    private final UserSpacesuitDataService userSpacesuitDataService;

    // Получение пользователей по ID планеты с пагинацией
    @GetMapping("users/{planetId}")
    public ResponseEntity<Page<UserData>> getUsersByPlanetId(
            @PathVariable Integer planetId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<UserData> userDataPage = userDataService.getUsersByPlanetId(planetId, pageable);

        HttpHeaders headers = PaginationUtil.createPaginationHeaders(userDataPage);

        return new ResponseEntity<>(userDataPage, headers, HttpStatus.OK);
    }


    // Получение всех данных пользователей с пагинацией
    @GetMapping("users/data")
    public ResponseEntity<Page<UserData>> getAllUsersData(Principal principal,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<UserData> userDataPage = userDataService.getAllUsersData(principal, pageable);

        HttpHeaders headers = PaginationUtil.createPaginationHeaders(userDataPage);

        return new ResponseEntity<>(userDataPage, headers, HttpStatus.OK);
    }

    // Получение данных текущего пользователя
    @GetMapping("current-user/data")
    public ResponseEntity<UserData> getCurrUserData(Principal principal) {
        Optional<UserData> userData = userDataService.getCurrUserData(principal);
        return userData.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.ok(null));
    }

    // Получение статусов скафандров текущего пользователя
    @GetMapping("current-user/spacesuit")
    public ResponseEntity<List<Status>> getCurrUserSpacesuitData(Principal principal,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<UserSpacesuitData> list = userSpacesuitDataService.getCurrUserSpacesuitData(principal, pageable);

        if (!list.isEmpty()) {
            // Если список не пуст, вернуть статусы скафандров
            return ResponseEntity.ok(list.stream().map(UserSpacesuitData::getStatus).collect(Collectors.toList()));
        }
        // Если данных нет, возвращаем пустой список
        return ResponseEntity.ok(List.of());
    }

    @ExceptionHandler(value = {UserNotCompletedRegistrationException.class, NoSpacesuitDataException.class})
    public ResponseEntity<?> handleIncorrectRequestExceptions(RuntimeException ex) {
        return ResponseEntity.badRequest().body("Incorrect request: " + ex);
    }
}
