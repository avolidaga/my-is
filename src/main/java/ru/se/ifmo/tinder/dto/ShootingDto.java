package ru.se.ifmo.tinder.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShootingDto {
    private Boolean isKronbars;
    private String username;
}
