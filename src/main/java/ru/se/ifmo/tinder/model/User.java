package ru.se.ifmo.tinder.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

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
    @JoinColumn(name="user_spacesuit_data_id")
    private UserSpacesuitData userSpacesuitDataId;
    @ManyToOne
    @JoinColumn(name="user_data_id")
    private UserData user_data_id;
    @ManyToOne
    @JoinColumn(name="role_id")
    private Roles role;
    @ManyToMany
    @JoinTable(
            name = "user_connections",
            joinColumns = @JoinColumn(name = "user_id_1"),
            inverseJoinColumns = @JoinColumn(name = "user_id_2")
    )
    private Set<User> connections;

}
