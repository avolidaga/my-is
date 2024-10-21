package ru.se.ifmo.tinder.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.se.ifmo.tinder.model.Roles;
import ru.se.ifmo.tinder.model.UserData;
import ru.se.ifmo.tinder.model.UserSpacesuitData;

import java.util.Set;

@Data
@Builder
public class AuthUserDto {

    private Long id;

    private String credentials;

    @NotBlank(message = "Username must not be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Role must not be blank")
    private Roles role;
}
