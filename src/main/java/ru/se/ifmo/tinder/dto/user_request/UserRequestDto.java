package ru.se.ifmo.tinder.dto.user_request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.se.ifmo.tinder.dto.spacesuit_data.UserSpacesuitDataDto;
import ru.se.ifmo.tinder.model.enums.RequestStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    @NotNull
    private Long userRequestId;

    @NotNull
    private UserSpacesuitDataDto userSpacesuitData;

    @NotNull
    private RequestStatus status;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

