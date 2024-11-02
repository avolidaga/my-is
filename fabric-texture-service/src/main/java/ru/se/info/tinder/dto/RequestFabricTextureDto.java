package ru.se.info.tinder.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RequestFabricTextureDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
}
