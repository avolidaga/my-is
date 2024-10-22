package ru.se.ifmo.tinder.mapper;

import ru.se.ifmo.tinder.dto.fabric_texture.CreateFabricTextureDto;
import ru.se.ifmo.tinder.dto.fabric_texture.FabricTextureDto;
import ru.se.ifmo.tinder.model.FabricTexture;

public class FabricTextureMapper {
    public static FabricTexture toEntityFabricTexture(CreateFabricTextureDto createFabricTextureDto) {
        return FabricTexture.builder()
                .name(createFabricTextureDto.getName())
                .description(createFabricTextureDto.getDescription())
                .build();
    }

    public static FabricTexture toEntityFabricTexture(FabricTextureDto dto) {
        return FabricTexture.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .id(dto.getId())
                .build();
    }

    public static FabricTextureDto toDtoFabricTexture(FabricTexture fabricTexture) {
        return FabricTextureDto.builder()
                .id(fabricTexture.getId())
                .name(fabricTexture.getName())
                .description((fabricTexture.getDescription()))
                .build();
    }
}
