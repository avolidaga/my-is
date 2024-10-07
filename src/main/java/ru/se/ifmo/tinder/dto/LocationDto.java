package ru.se.ifmo.tinder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import ru.se.ifmo.tinder.model.UserData;

import java.util.Set;

@Data
@Builder
public class LocationDto {
    @Positive
    private Integer id;
    @NotBlank
    private String name;
    private String description;
    private Double temperature;
    Set<UserData> usersLocation;
}
