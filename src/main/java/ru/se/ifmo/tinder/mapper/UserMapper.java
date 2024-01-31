package ru.se.ifmo.tinder.mapper;

import ru.se.ifmo.tinder.dto.UserDto;
import ru.se.ifmo.tinder.model.User;

public class UserMapper {
    public static User toEntityUser(UserDto userDto) {
        return User.builder().username(userDto.getUsername()).password(userDto.getPassword()).build();
    }
    public static UserDto toDtoUser(User user) {
        return UserDto.builder().username(user.getUsername()).password(user.getPassword()).build();
    }
}
