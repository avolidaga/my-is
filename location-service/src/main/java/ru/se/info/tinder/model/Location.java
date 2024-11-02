package ru.se.info.tinder.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "location")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long id;

    @NotNull
    private String name;
    private String description;
    private Double temperature;

    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "locations")
    Set<UserData> userData;
}
