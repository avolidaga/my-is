package ru.se.info.tinder.model;

import javax.persistence.*;
import lombok.*;

import java.util.Set;

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
    private String username;
    private String password;

    @OneToOne(mappedBy = "ownerUser")
    @EqualsAndHashCode.Exclude
    private UserData userData; //TODO добавить сохранение пользователя + изменить бд

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Roles role;

    @ManyToMany
    @JoinTable(
            name = "user_connections",
            joinColumns = @JoinColumn(name = "user_id_1"),
            inverseJoinColumns = @JoinColumn(name = "user_id_2")
    )
    @EqualsAndHashCode.Exclude
    private Set<User> matchedUsers;
}
