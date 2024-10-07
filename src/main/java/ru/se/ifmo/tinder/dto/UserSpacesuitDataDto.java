package ru.se.ifmo.tinder.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import ru.se.ifmo.tinder.model.FabricTexture;
import ru.se.ifmo.tinder.model.enums.Status;

@Data
@Builder
public class UserSpacesuitDataDto {

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
    private Integer foot_size;

    @NotNull(message = "Height must not be null")
    @Positive(message = "Height must be a positive number")
    @Max(value = 300, message = "Height must be less than or equal to 300")
    private Integer height;

    @NotNull(message = "Fabric texture ID must not be null")
    @Positive(message = "Fabric texture ID must be a positive number")
    private Integer fabric_texture_id;

}




