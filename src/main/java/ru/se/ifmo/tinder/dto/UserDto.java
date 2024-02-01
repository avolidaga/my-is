package ru.se.ifmo.tinder.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class UserDto{

    private String username;
    private String password;
//    private String firstName;
//    private String lastName;
//    private Integer userSpacesuitDataId;
//    private Integer userDataId;
//    @NotEmpty(message = "Email should not be empty")
//    private String username;
//    @NotEmpty(message = "Password should not be empty")
//    private String password;
}
