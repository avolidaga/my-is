package ru.se.ifmo.tinder.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDto {

    @NotBlank(message = "Credentials must not be blank")
    private String credentials;

    @NotBlank(message = "Role must not be blank")
    private String role;
}

