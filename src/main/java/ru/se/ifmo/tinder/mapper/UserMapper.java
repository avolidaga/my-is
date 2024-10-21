package ru.se.ifmo.tinder.mapper;

import ru.se.ifmo.tinder.dto.user.AuthUserDto;
import ru.se.ifmo.tinder.dto.user.CreateUserDto;
import ru.se.ifmo.tinder.dto.user.UserDto;
import ru.se.ifmo.tinder.model.User;

public class UserMapper {
    public static User toEntityUser(CreateUserDto createUserDto) {
        return User.builder()
                .username(createUserDto.getUsername())
                .password(createUserDto.getPassword())
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

    public static AuthUserDto toDtoAuthUser(User user, String credentials) {
        return AuthUserDto.builder()
                .username(user.getUsername())
                .id(user.getId())
                .role(user.getRole())
                .credentials(credentials)
                .build();
    }
}
