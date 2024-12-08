package ru.se.info.tinder.model;

import javax.persistence.*;
import javax.validation.constraints.PastOrPresent;

import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "user_connections")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id_1")
    private UserData userData1;

    @ManyToOne
    @JoinColumn(name = "user_id_2")
    private UserData userData2;

    @PastOrPresent
    private LocalDate matchDate;
}
