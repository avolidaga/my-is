package ru.se.ifmo.tinder.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.se.ifmo.tinder.dto.UserSpacesuitDataDto;
import ru.se.ifmo.tinder.service.UserSpacesuitDataService;

import java.security.Principal;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/spacesuit-form")
@SecurityRequirement(name = "basicAuth")
public class FormSpacesuitController {

    private final UserSpacesuitDataService userSpacesuitDataService;

    // Метод для отправки данных о скафандре с валидацией
    @PostMapping()
    public ResponseEntity<Integer> submitForm(@Valid @RequestBody UserSpacesuitDataDto userSpacesuitDataDto, Principal principal) {
        Integer id = userSpacesuitDataService.insertUserSpacesuitData(
                userSpacesuitDataDto.getHead(),
                userSpacesuitDataDto.getChest(),
                userSpacesuitDataDto.getWaist(),
                userSpacesuitDataDto.getHips(),
                userSpacesuitDataDto.getFoot_size(),
                userSpacesuitDataDto.getHeight(),
                userSpacesuitDataDto.getFabric_texture_id(),
                principal
        );
        return ResponseEntity.ok(id);
    }
}
