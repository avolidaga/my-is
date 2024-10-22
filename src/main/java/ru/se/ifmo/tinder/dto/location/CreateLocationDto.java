package ru.se.ifmo.tinder.dto.location;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateLocationDto {
    @NotBlank
    private String name;
    private String description;
    private Double temperature;
}
