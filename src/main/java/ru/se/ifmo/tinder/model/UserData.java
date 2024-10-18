package ru.se.ifmo.tinder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import ru.se.ifmo.tinder.model.enums.Sex;

import java.time.LocalDate;
import java.util.Set;

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
    private String firstname;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "user_data_location",
            joinColumns = @JoinColumn(name = "user_data_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id"))
    Set<ru.se.ifmo.tinder.model.Location> locations;
}
