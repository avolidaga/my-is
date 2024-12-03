package ru.se.info.tinder.model;

import javax.persistence.*;

import lombok.*;
import ru.se.info.tinder.model.enums.RoleName;

@Entity
@Table(name = "roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Enumerated(EnumType.STRING)
    private RoleName roleName;
}
