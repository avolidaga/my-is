package ru.se.info.tinder.mapper;

import ru.se.ifmo.tinder.dto.spacesuit_data.CreateSpacesuitDataDto;
import ru.se.ifmo.tinder.dto.spacesuit_data.UpdateSpacesuitDataDto;
import ru.se.ifmo.tinder.dto.spacesuit_data.SpacesuitDataDto;
import ru.se.ifmo.tinder.model.FabricTexture;
import ru.se.ifmo.tinder.model.SpacesuitData;

import java.time.LocalDateTime;

public class SpacesuitDataMapper {
    public static SpacesuitData toEntitySpacesuitData(CreateSpacesuitDataDto dto, FabricTexture fabricTexture) {
        return SpacesuitData.builder()
                .head(dto.getHead())
                .chest(dto.getChest())
                .waist(dto.getWaist())
                .hips(dto.getHips())
                .footSize(dto.getFootSize())
                .height(dto.getHeight())
                .fabricTexture(fabricTexture)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static SpacesuitData toEntitySpacesuitData(UpdateSpacesuitDataDto dto, SpacesuitData spacesuitData, FabricTexture fabricTexture) {
        return SpacesuitData.builder()
                .id(spacesuitData.getId())
                .head(dto.getHead())
                .chest(dto.getChest())
                .waist(dto.getWaist())
                .hips(dto.getHips())
                .footSize(dto.getFootSize())
                .height(dto.getHeight())
                .fabricTexture(fabricTexture)
                .createdAt(spacesuitData.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static SpacesuitDataDto toSpacesuitDataDto(SpacesuitData entity) {
        return SpacesuitDataDto.builder()
                .head(entity.getHead())
                .chest(entity.getChest())
                .waist(entity.getWaist())
                .hips(entity.getHips())
                .footSize(entity.getFootSize())
                .height(entity.getHeight())
                .fabricTextureDto(FabricTextureMapper.toDtoFabricTexture(entity.getFabricTexture()))
                .id(entity.getId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
