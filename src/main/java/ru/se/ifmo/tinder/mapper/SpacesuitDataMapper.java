package ru.se.ifmo.tinder.mapper;

import ru.se.ifmo.tinder.dto.UserSpacesuitDataDto;
import ru.se.ifmo.tinder.model.FabricTexture;
import ru.se.ifmo.tinder.model.UserSpacesuitData;

public class SpacesuitDataMapper {
    public static UserSpacesuitData toEntitySpacesuitData(UserSpacesuitDataDto dto, FabricTexture fabricTexture) {
        return UserSpacesuitData.builder()
                .head(dto.getHead())
                .chest(dto.getChest())
                .waist(dto.getWaist())
                .hips(dto.getHips())
                .foot_size(dto.getFoot_size())
                .height(dto.getHeight())
                .fabricTextureId(fabricTexture)
                .build();
    }
}
