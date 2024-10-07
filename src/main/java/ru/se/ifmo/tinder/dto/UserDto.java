package ru.se.ifmo.tinder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.se.ifmo.tinder.model.Roles;
import ru.se.ifmo.tinder.model.UserData;
import ru.se.ifmo.tinder.model.UserSpacesuitData;

@Data
@Builder
public class UserDto {

    @NotBlank(message = "Username must not be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

//    private String firstName;
//    private String lastName;
//    private UserSpacesuitData userSpacesuitDataId;
//    private UserData userDataId;
//    private Roles role_id;

}

