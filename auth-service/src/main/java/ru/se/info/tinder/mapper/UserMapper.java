package ru.se.info.tinder.mapper;

import ru.se.info.tinder.dto.AuthUserDto;
import ru.se.info.tinder.dto.RequestUserDto;
import ru.se.info.tinder.dto.UserDto;
import ru.se.info.tinder.model.User;

public class UserMapper {
    public static User toEntityUser(RequestUserDto requestUserDto) {
        return User.builder()
                .username(requestUserDto.getUsername())
                .password(requestUserDto.getPassword())
                .build();
    }

    public static UserDto toDtoUser(User user) {
        return UserDto.builder()
                .username(user.getUsername())
                .userData(UserDataMapper.toUserDataDto(user.getUserData()))
                .id(user.getId())
                .role(user.getRole())
                .build();
    }

    public static User toEntityUser(RequestUserDto requestUserDto, User oldUser) {
        return User.builder()
                .username(requestUserDto.getUsername())
                .password(requestUserDto.getPassword())
                .id(oldUser.getId())
                .matchedUsers(oldUser.getMatchedUsers())
                .role(oldUser.getRole())
                .userData(oldUser.getUserData())
                .build();
    }

    public static AuthUserDto toDtoAuthUser(User user, String credentials) {
        return AuthUserDto.builder()
                .username(user.getUsername())
                .id(user.getId())
                .role(user.getRole())
                .credentials(credentials)
                .build();
    }
}
