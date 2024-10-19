package ru.se.ifmo.tinder.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

@Entity
@Table(name = "user_connections")
public class UserConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id_1")
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user_id_2")
    private User user2;

    @PastOrPresent
    private LocalDate dateTime;
}
