package ru.se.info.tinder.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.se.info.tinder.model.enums.RequestStatus;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

