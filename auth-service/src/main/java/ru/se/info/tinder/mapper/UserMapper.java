package ru.se.info.tinder.mapper;

import ru.se.info.tinder.dto.RequestUserDto;
import ru.se.info.tinder.dto.UserDto;
import ru.se.info.tinder.model.UserEntity;

public class UserMapper {
    public static UserEntity toEntityUser(RequestUserDto requestUserDto) {
        return UserEntity.builder()
                .username(requestUserDto.getUsername())
                .password(requestUserDto.getPassword())
                .build();
    }

    public static UserDto toDtoUser(UserEntity userEntity) {
        return UserDto.builder()
                .username(userEntity.getUsername())
                .userData((userEntity.getUserData() == null) ? null : UserDataMapper.toUserDataDto(userEntity.getUserData()))
                .id(userEntity.getId())
                .role(userEntity.getRole())
                .build();
    }

    public static UserEntity toEntityUser(RequestUserDto requestUserDto, UserEntity oldUserEntity) {
        return UserEntity.builder()
                .username(requestUserDto.getUsername())
                .password(requestUserDto.getPassword())
                .id(oldUserEntity.getId())
                .matchedUserEntities(oldUserEntity.getMatchedUserEntities())
                .role(oldUserEntity.getRole())
                .userData(oldUserEntity.getUserData())
                .build();
    }
}
