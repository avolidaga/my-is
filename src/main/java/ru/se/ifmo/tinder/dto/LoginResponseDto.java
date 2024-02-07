package ru.se.ifmo.tinder.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDto {
    private String credentials;
    private String role;
}
