package ru.se.ifmo.tinder.mapper;

import ru.se.ifmo.tinder.dto.spacesuit_data.CreateSpacesuitDataDto;
import ru.se.ifmo.tinder.dto.spacesuit_data.UserSpacesuitDataDto;
import ru.se.ifmo.tinder.model.FabricTexture;
import ru.se.ifmo.tinder.model.UserSpacesuitData;
import ru.se.ifmo.tinder.model.enums.RequestStatus;

import java.time.LocalDateTime;

public class SpacesuitDataMapper {
    public static UserSpacesuitData toEntitySpacesuitData(CreateSpacesuitDataDto dto, FabricTexture fabricTexture) {
        return UserSpacesuitData.builder()
                .head(dto.getHead())
                .chest(dto.getChest())
                .waist(dto.getWaist())
                .hips(dto.getHips())
                .footSize(dto.getFootSize())
                .height(dto.getHeight())
                .fabricTexture(fabricTexture)
                .status(RequestStatus.NEW)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static UserSpacesuitDataDto toSpacesuitDataDto(UserSpacesuitData entity) {
        return UserSpacesuitDataDto.builder()
                .head(entity.getHead())
                .chest(entity.getChest())
                .waist(entity.getWaist())
                .hips(entity.getHips())
                .footSize(entity.getFootSize())
                .height(entity.getHeight())
                .fabricTextureDto(FabricTextureMapper.toDtoFabricTexture(entity.getFabricTexture()))
                .status(entity.getStatus())
                .id(entity.getId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
