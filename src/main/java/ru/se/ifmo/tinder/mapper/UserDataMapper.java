package ru.se.ifmo.tinder.mapper;

import ru.se.ifmo.tinder.dto.CreateUserDataDto;
import ru.se.ifmo.tinder.model.Location;
import ru.se.ifmo.tinder.model.UserData;

import java.util.Set;

public class UserDataMapper {
    public static UserData toEntityUserData(CreateUserDataDto createUserDataDto, Set<Location> locations) {
        return UserData.builder()
                .birthdate(createUserDataDto.getBirth_date())
                .sex(createUserDataDto.getSex())
                .weight(createUserDataDto.getWeight())
                .height(createUserDataDto.getHeight())
                .hairColor(createUserDataDto.getHair_color())
                .firstname(createUserDataDto.getFirstname())
                .locations(locations)
                .build();
    }
}
