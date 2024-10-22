package ru.se.ifmo.tinder.mapper;

import ru.se.ifmo.tinder.dto.user_data.CreateUserDataDto;
import ru.se.ifmo.tinder.dto.user_data.UpdateUserDataDto;
import ru.se.ifmo.tinder.dto.user_data.UserDataDto;
import ru.se.ifmo.tinder.model.Location;
import ru.se.ifmo.tinder.model.UserData;

import java.time.LocalDateTime;
import java.util.Set;

public class UserDataMapper {
    public static UserData toEntityUserData(CreateUserDataDto createUserDataDto, Set<Location> locations) {
        return UserData.builder()
                .birthDate(createUserDataDto.getBirthDate())
                .sex(createUserDataDto.getSex())
                .weight(createUserDataDto.getWeight())
                .height(createUserDataDto.getHeight())
                .hairColor(createUserDataDto.getHairColor())
                .firstname(createUserDataDto.getFirstname())
                .lastname(createUserDataDto.getLastname())
                .locations(locations)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static UserData toEntityUserData(UpdateUserDataDto updateUserDataDto, Set<Location> locations, UserData userData) {
        return UserData.builder()
                .id(updateUserDataDto.getId())
                .birthDate(updateUserDataDto.getBirthDate())
                .sex(updateUserDataDto.getSex())
                .weight(updateUserDataDto.getWeight())
                .height(updateUserDataDto.getHeight())
                .hairColor(updateUserDataDto.getHairColor())
                .firstname(updateUserDataDto.getFirstname())
                .lastname(updateUserDataDto.getLastname())
                .locations(locations)
                .createdAt(userData.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .ownerUser(userData.getOwnerUser())
                .build();
    }

    public static UserDataDto toUserDataDto(UserData userData) {
        return UserDataDto.builder()
                .birthDate(userData.getBirthDate())
                .sex(userData.getSex())
                .weight(userData.getWeight())
                .height(userData.getHeight())
                .hairColor(userData.getHairColor())
                .firstname(userData.getFirstname())
                .lastname(userData.getLastname())
                .locations(userData.getLocations().stream().map(Location::getId).toList())
                .createdAt(userData.getCreatedAt())
                .ownerUserId(userData.getOwnerUser().getId())
                .updatedAt(userData.getUpdatedAt())
                .id(userData.getId())
                .build();
    }
}
