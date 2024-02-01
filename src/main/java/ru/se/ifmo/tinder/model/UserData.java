package ru.se.ifmo.tinder.model;

import jakarta.persistence.*;
import lombok.*;
import ru.se.ifmo.tinder.model.enums.Location;
import ru.se.ifmo.tinder.model.enums.Sex;

import java.time.LocalDate;

@Entity
@Table(name = "user_data")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate birthdate;
    private Sex sex;
    private Integer weight;
    private Integer height;
    private String hairColor;
    private Location location;
}
