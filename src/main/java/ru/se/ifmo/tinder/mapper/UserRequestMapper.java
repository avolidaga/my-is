package ru.se.ifmo.tinder.mapper;

import ru.se.ifmo.tinder.dto.user_request.UserRequestDto;
import ru.se.ifmo.tinder.model.UserRequest;

public class UserRequestMapper {

    public static UserRequestDto toUserRequestDto(UserRequest userRequest) {
        return UserRequestDto.builder()
                .userSpacesuitData(SpacesuitDataMapper.toSpacesuitDataDto(userRequest.getUserSpacesuitData()))
                .userRequestId(userRequest.getUserRequestId())
                .status(userRequest.getStatus())
                .createdAt(userRequest.getCreatedAt())
                .updatedAt(userRequest.getUpdatedAt())
                .build();
    }
}
