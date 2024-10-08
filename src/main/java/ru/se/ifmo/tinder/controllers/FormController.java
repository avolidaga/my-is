package ru.se.ifmo.tinder.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.se.ifmo.tinder.dto.UserDataDto;
import ru.se.ifmo.tinder.service.UserDataService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-form")
@SecurityRequirement(name = "basicAuth")
public class FormController {

    private final UserDataService userDataService;

    // Метод для отправки формы с валидацией
    @PostMapping
    public ResponseEntity<Integer> submitForm(@Valid @RequestBody UserDataDto userDataDto, Principal principal) {
        Integer id = userDataService.insertUserData(userDataDto, principal);
        return ResponseEntity.ok(id);
    }
}
