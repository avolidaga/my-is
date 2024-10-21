package ru.se.ifmo.tinder.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "users")
@Setter
@Getter
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    @OneToMany(mappedBy = "ownerUser")
    private Set<UserSpacesuitData> userSpacesuitDataSet;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_data_id", referencedColumnName = "user_data_id")
    private UserData userData;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Roles role;

    @ManyToMany
    @JoinTable(
            name = "user_connections",
            joinColumns = @JoinColumn(name = "user_id_1"),
            inverseJoinColumns = @JoinColumn(name = "user_id_2")
    )
    private Set<User> matchedUsers;
}
