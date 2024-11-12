package ru.se.info.tinder.mapper;


import ru.se.info.tinder.dto.FabricTextureDto;
import ru.se.info.tinder.dto.RequestFabricTextureDto;
import ru.se.info.tinder.model.FabricTexture;

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
