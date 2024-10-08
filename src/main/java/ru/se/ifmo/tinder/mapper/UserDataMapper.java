package ru.se.ifmo.tinder.mapper;

import ru.se.ifmo.tinder.dto.UserDataDto;
import ru.se.ifmo.tinder.model.Location;
import ru.se.ifmo.tinder.model.UserData;

import java.util.Set;

public class UserDataMapper {
    public static UserData toEntityUserData(UserDataDto userDataDto, Set<Location> locations) {
        return UserData.builder()
                .birthdate(userDataDto.getBirth_date())
                .sex(userDataDto.getSex())
                .weight(userDataDto.getWeight())
                .height(userDataDto.getHeight())
                .hairColor(userDataDto.getHair_color())
                .firstname(userDataDto.getFirstname())
                .locations(locations)
                .build();
    }
}
