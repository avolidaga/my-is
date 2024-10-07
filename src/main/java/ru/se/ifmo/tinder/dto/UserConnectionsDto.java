package ru.se.ifmo.tinder.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserConnectionsDto {

    @NotNull(message = "User data ID 2 must not be null")
    @Positive(message = "User data ID 2 must be a positive number")
    private Integer user_data_id_2;
}

