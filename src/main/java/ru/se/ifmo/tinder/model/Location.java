package ru.se.ifmo.tinder.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "location")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long id;

    @NotNull
    private String name;
    private String description;
    private Double temperature;

    @ManyToMany(mappedBy = "locations")
    Set<UserData> userData;
}
