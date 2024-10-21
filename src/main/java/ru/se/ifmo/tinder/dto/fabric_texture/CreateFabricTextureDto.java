package ru.se.ifmo.tinder.dto.fabric_texture;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@Getter
public class CreateFabricTextureDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
}
