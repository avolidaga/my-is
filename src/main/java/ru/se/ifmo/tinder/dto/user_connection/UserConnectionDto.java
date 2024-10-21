package ru.se.ifmo.tinder.dto.user_connection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.se.ifmo.tinder.dto.user.UserDto;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserConnectionDto {

    private Long id;
    private UserDto userDto1;
    private UserDto userDto2;
    private LocalDate matchDate;
}

