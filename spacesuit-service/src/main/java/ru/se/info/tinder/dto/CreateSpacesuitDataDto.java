package ru.se.info.tinder.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateSpacesuitDataDto {
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
    private Long fabricTextureId;
}




