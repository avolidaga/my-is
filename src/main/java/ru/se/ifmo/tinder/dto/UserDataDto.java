package ru.se.ifmo.tinder.dto;

import lombok.Builder;
import lombok.Data;
import ru.se.ifmo.tinder.model.enums.Location;
import ru.se.ifmo.tinder.model.enums.Sex;

import java.time.LocalDate;

@Data
@Builder
public class UserDataDto {
    private LocalDate birth_date;
    private Sex sex;
    private Integer weight;
    private Integer height;
    private String hair_color;
    private Location location;
    private String firstname;
}
