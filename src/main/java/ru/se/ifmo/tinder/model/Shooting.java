package ru.se.ifmo.tinder.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shooting")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Shooting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean isKronbars;
    private String username;



}
