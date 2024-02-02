package ru.se.ifmo.tinder.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    private String username;
    private String password;
    @ManyToOne
    @JoinColumn(name="user_data_id")
    private UserData user_data_id;
//    private String firstName;

}
