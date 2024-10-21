package ru.se.ifmo.tinder.dto.fabric_texture;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FabricTextureDto {
    private Long id;
    private String name;
    private String description;
}
