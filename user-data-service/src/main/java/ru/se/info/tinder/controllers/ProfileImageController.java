package ru.se.info.tinder.controllers;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.se.info.tinder.dto.ProfileImageResponse;
import ru.se.info.tinder.service.ProfileImageService;

import java.io.OutputStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-data")
@OpenAPIDefinition(
        servers = {@Server(url = "http://localhost:8080")}
)
public class ProfileImageController {

    private final ProfileImageService profileImageService;

    @PostMapping(path = "{userDataId}/image/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public ProfileImageResponse uploadProfileImage(@PathVariable("userDataId") Long userDataId,
                                                   @RequestPart("file") MultipartFile file) {
        return profileImageService.uploadProfileImageByUserDataId(userDataId, file);
    }

    @DeleteMapping("{userDataId}/image/{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public ProfileImageResponse deleteProfileImageById(@PathVariable Long userDataId,
                                                       @PathVariable String id) {
        return profileImageService.deleteProfileImageById(userDataId, id);
    }

    @GetMapping(path = "{userDataId}/image/{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public String getProfileImageUrlById(@PathVariable Long userDataId,
                                         @PathVariable String id) {
        return profileImageService.getProfileImageUrlById(userDataId, id);
    }
}
