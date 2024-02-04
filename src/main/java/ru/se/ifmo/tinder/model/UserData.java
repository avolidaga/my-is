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
    @Column(name = "user_data_id")
    private Integer id;
    @Column(name = "birth_date")
    private LocalDate birthdate;
    @Column(name = "sex")
    @Enumerated(EnumType.STRING)
    private Sex sex;
    private Integer weight;
    private Integer height;
    @Column(name = "hair_color")
    private String hairColor;
    @Enumerated(EnumType.STRING)
    private Location location;
    String firstname;
}
