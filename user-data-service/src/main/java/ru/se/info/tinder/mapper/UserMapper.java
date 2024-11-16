package ru.se.info.tinder.mapper;

import ru.se.info.tinder.dto.UserDto;
import ru.se.info.tinder.model.User;

public class UserMapper {

    public static UserDto toDtoUser(User user) {
        return UserDto.builder()
                .username(user.getUsername())
                .userData(UserDataMapper.toUserDataDto(user.getUserData()))
                .id(user.getId())
                .role(user.getRole())
                .build();
    }
}