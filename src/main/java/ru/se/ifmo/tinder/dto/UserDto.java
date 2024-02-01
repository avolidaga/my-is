package ru.se.ifmo.tinder.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

//как то добавить нужные значения полей
@Data
@Builder
public class UserDto{

    private String username;
    private String password;
//    private String firstName;
//    private String lastName;
//    private Integer userSpacesuitDataId; // тут либо тип int либо класса spacesuit??
//    private Integer userDataId;
//    @NotEmpty(message = "Email should not be empty")
//    private String username;
//    @NotEmpty(message = "Password should not be empty")
//    private String password;
}
