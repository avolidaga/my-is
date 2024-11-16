package ru.se.info.tinder.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @OneToOne(mappedBy = "userData")
    @EqualsAndHashCode.Exclude
    private User ownerUser;
    private String firstname;
    private String lastname;
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Sex sex;
    private Integer weight;
    private Integer height;
    private String hairColor;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
