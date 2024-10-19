package ru.se.ifmo.tinder.model;

import jakarta.persistence.*;
import ru.se.ifmo.tinder.model.enums.RoleName;

@Entity
@Table(name = "roles")
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Enumerated(EnumType.STRING)
    private RoleName roleName;
}
