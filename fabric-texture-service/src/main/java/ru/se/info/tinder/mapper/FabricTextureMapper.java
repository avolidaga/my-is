package ru.se.info.tinder.mapper;

import ru.se.ifmo.tinder.dto.fabric_texture.RequestFabricTextureDto;
import ru.se.ifmo.tinder.dto.fabric_texture.FabricTextureDto;
import ru.se.ifmo.tinder.model.FabricTexture;

public class FabricTextureMapper {
    public static FabricTexture toEntityFabricTexture(RequestFabricTextureDto requestFabricTextureDto) {
        return FabricTexture.builder()
                .name(requestFabricTextureDto.getName())
                .description(requestFabricTextureDto.getDescription())
                .build();
    }

    public static FabricTexture toEntityFabricTexture(Long id, RequestFabricTextureDto requestFabricTextureDto) {
        return FabricTexture.builder()
                .name(requestFabricTextureDto.getName())
                .description(requestFabricTextureDto.getDescription())
                .id(id)
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
