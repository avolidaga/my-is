package ru.se.ifmo.tinder.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.se.ifmo.tinder.dto.user_data.UserDataDto;
import ru.se.ifmo.tinder.model.Roles;

@Data
@Builder
public class UserDto {

    private Long id;

    @NotBlank(message = "Username must not be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    private UserDataDto userData;

    @NotBlank(message = "Role must not be blank")
    private Roles role;
}
