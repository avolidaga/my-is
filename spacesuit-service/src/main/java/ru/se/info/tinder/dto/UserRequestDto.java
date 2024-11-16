package ru.se.info.tinder.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.se.info.tinder.model.enums.RequestStatus;

import java.time.LocalDateTime;

@Data
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserRequestDto {

    @NotNull
    private Long userRequestId;

    @NotNull
    private SpacesuitDataDto userSpacesuitData;

    @NotNull
    private RequestStatus status;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

