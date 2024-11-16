package ru.se.info.tinder.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import ru.se.info.tinder.model.enums.RequestStatus;

import java.time.LocalDateTime;

@Data
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SpacesuitDataDto {

    @NotNull
    private Long id;

    @NotNull(message = "Head measurement must not be null")
    @Positive(message = "Head measurement must be a positive number")
    private Integer head;

    @NotNull(message = "Chest measurement must not be null")
    @Positive(message = "Chest measurement must be a positive number")
    private Integer chest;

    @NotNull(message = "Waist measurement must not be null")
    @Positive(message = "Waist measurement must be a positive number")
    private Integer waist;

    @NotNull(message = "Hips measurement must not be null")
    @Positive(message = "Hips measurement must be a positive number")
    private Integer hips;

    @NotNull(message = "Foot size must not be null")
    @Positive(message = "Foot size must be a positive number")
    private Integer footSize;

    @NotNull(message = "Height must not be null")
    @Positive(message = "Height must be a positive number")
    @Max(value = 300, message = "Height must be less than or equal to 300")
    private Integer height;

    @NotNull
    private RequestStatus status;

    @NotNull
    private FabricTextureDto fabricTextureDto;
    @NotNull
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}




