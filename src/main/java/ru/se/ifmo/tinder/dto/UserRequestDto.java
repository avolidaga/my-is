package ru.se.ifmo.tinder.dto;

import lombok.Builder;
import lombok.Data;
import ru.se.ifmo.tinder.model.enums.Status;

@Data
@Builder
public class UserRequestDto {
    private UserSpacesuitDataDto user_spacesuit_data_id;
    private Status status;
}
