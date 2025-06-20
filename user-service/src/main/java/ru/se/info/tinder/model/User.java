package ru.se.info.tinder.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private String citizenship;
    private String role;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
