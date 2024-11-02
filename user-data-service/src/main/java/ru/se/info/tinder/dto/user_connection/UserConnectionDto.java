package ru.se.info.tinder.dto.user_connection;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import ru.se.ifmo.tinder.dto.user.UserDto;

import java.time.LocalDate;

@Data
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserConnectionDto {

    private Long id;
    private UserDto userDto1;
    private UserDto userDto2;
    private LocalDate matchDate;
}

