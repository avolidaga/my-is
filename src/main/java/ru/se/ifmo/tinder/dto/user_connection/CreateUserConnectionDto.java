package ru.se.ifmo.tinder.dto.user_connection;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateUserConnectionDto {

    @NotNull(message = "User data ID 2 must not be null")
    @Positive(message = "User data ID 2 must be a positive number")
    private Long userDataId;
}

