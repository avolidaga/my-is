package ru.se.info.tinder.mapper;

import ru.se.info.tinder.dto.MeetingMessage;
import ru.se.info.tinder.dto.UserRequestDto;
import ru.se.info.tinder.model.UserRequest;

public class MeetingParticipantMapper {

    public static UserRequestDto toUserRequestDto(UserRequest userRequest) {
        return UserRequestDto.builder()
                .userSpacesuitData(MeetingMapper.toSpacesuitDataDto(userRequest.getSpacesuitData()))
                .userRequestId(userRequest.getUserRequestId())
                .status(userRequest.getStatus())
                .createdAt(userRequest.getCreatedAt())
                .updatedAt(userRequest.getUpdatedAt())
                .build();
    }

    public static MeetingMessage toSpacesuitRequestMsg(UserRequestDto userRequestDto) {
        return MeetingMessage.builder()
                .spacesuitDataId(userRequestDto.getUserSpacesuitData().getId())
                .createdAt(userRequestDto.getCreatedAt())
                .status(userRequestDto.getStatus())
                .updatedAt(userRequestDto.getUpdatedAt())
                .userRequestId(userRequestDto.getUserRequestId())
                .build();
    }
}
