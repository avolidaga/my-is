package ru.se.info.tinder.mapper;

import ru.se.info.tinder.dto.UserDataDto;
import ru.se.info.tinder.model.UserData;
public class UserDataMapper {

    public static UserDataDto toUserDataDto(UserData userData) {
        return UserDataDto.builder()
                .birthDate(userData.getBirthDate())
                .sex(userData.getSex())
                .weight(userData.getWeight())
                .height(userData.getHeight())
                .hairColor(userData.getHairColor())
                .firstname(userData.getFirstname())
                .lastname(userData.getLastname())
                .createdAt(userData.getCreatedAt())
                .ownerUserId(userData.getOwnerUser().getId())
                .updatedAt(userData.getUpdatedAt())
                .id(userData.getId())
                .build();
    }
}
