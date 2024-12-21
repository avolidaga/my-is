package ru.se.info.tinder.mapper;

import ru.se.info.tinder.dto.SpacesuitRequestMessage;
import ru.se.info.tinder.dto.UpdateSpacesuitDataDto;
import ru.se.info.tinder.dto.UserRequestDto;
import ru.se.info.tinder.model.UserRequest;

public class UserRequestMapper {

    public static UserRequestDto toUserRequestDto(UserRequest userRequest) {
        return UserRequestDto.builder()
                .userSpacesuitData(SpacesuitDataMapper.toSpacesuitDataDto(userRequest.getSpacesuitData()))
                .userRequestId(userRequest.getUserRequestId())
                .status(userRequest.getStatus())
                .createdAt(userRequest.getCreatedAt())
                .updatedAt(userRequest.getUpdatedAt())
                .build();
    }

    public static SpacesuitRequestMessage toSpacesuitRequestMsg(UserRequestDto userRequestDto) {
        return SpacesuitRequestMessage.builder()
                .spacesuitDataId(userRequestDto.getUserSpacesuitData().getId())
                .createdAt(userRequestDto.getCreatedAt())
                .status(userRequestDto.getStatus())
                .updatedAt(userRequestDto.getUpdatedAt())
                .userRequestId(userRequestDto.getUserRequestId())
                .build();
    }
}
