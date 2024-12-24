package ru.se.info.tinder.mapper;

import ru.se.info.tinder.dto.CreateUserDataDto;
import ru.se.info.tinder.dto.UpdateUserDataDto;
import ru.se.info.tinder.dto.UserDataDto;
import ru.se.info.tinder.model.Location;
import ru.se.info.tinder.model.User;
import ru.se.info.tinder.model.UserData;

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
                .ownerUser(User.builder().id(createUserDataDto.getUserId()).build())
                .build();
    }

    public static UserData toEntityUserData(UpdateUserDataDto updateUserDataDto, Set<Location> locations, UserData userData) {
        return UserData.builder()
                .id(userData.getId())
                .birthDate(updateUserDataDto.getBirthDate())
                .profileImageId(userData.getProfileImageId())
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
                .profileImageId(userData.getProfileImageId())
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
