package ru.se.info.tinder.model;

import lombok.*;
import ru.se.info.tinder.model.enums.Sex;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "user_data")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_data_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User ownerUser;
    private String firstname;
    private String lastname;
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Sex sex;
    private Integer weight;
    private Integer height;
    private String hairColor;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_data_location",
            joinColumns = @JoinColumn(name = "user_data_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id"))
    Set<Location> locations;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
