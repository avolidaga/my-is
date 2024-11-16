package ru.se.info.tinder.mapper;


import ru.se.info.tinder.dto.FabricTextureDto;
import ru.se.info.tinder.model.FabricTexture;

public class FabricTextureMapper {

    public static FabricTexture toEntityFabricTexture(FabricTextureDto fabricTextureDto) {
        return FabricTexture.builder()
                .id(fabricTextureDto.getId())
                .name(fabricTextureDto.getName())
                .description(fabricTextureDto.getDescription())
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
