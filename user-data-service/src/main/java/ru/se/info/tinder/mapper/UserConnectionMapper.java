package ru.se.info.tinder.mapper;

import ru.se.info.tinder.dto.UserConnectionDto;
import ru.se.info.tinder.model.UserConnection;

public class UserConnectionMapper {
    public static UserConnectionDto toDtoUserConnection(UserConnection userConnection) {
        return UserConnectionDto.builder()
                .userDto1(UserDataMapper.toUserDataDto(userConnection.getUserData1()))
                .userDto2(UserDataMapper.toUserDataDto(userConnection.getUserData2()))
                .matchDate(userConnection.getMatchDate())
                .id(userConnection.getId())
                .build();
    }
}
