package ru.se.ifmo.tinder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import ru.se.ifmo.tinder.model.enums.Sex;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "user_data")
@Builder
@Getter
public class UserData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_data_id")
    private Long id;

    @OneToOne(mappedBy = "userData")
    private User ownerUser;
    private String firstname;
    private String lastname;
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Sex sex;
    private Integer weight;
    private Integer height;
    private String hairColor;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "user_data_location",
            joinColumns = @JoinColumn(name = "user_data_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id"))
    Set<ru.se.ifmo.tinder.model.Location> locations;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
