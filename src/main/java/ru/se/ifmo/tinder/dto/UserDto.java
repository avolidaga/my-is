package ru.se.ifmo.tinder.dto;

import lombok.Builder;
import lombok.Data;
import ru.se.ifmo.tinder.model.Roles;
import ru.se.ifmo.tinder.model.UserData;
import ru.se.ifmo.tinder.model.UserSpacesuitData;


@Data
@Builder
public class UserDto{


    private String username;
    private String password;
//    private String firstName;
//    private String lastName;
//    private UserSpacesuitData userSpacesuitDataId;
//    private UserData userDataId;
//   private Roles role_id;
//    @NotEmpty(message = "Email should not be empty")
//    private String username;
//    @NotEmpty(message = "Password should not be empty")
//    private String password;
}
