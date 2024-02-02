package ru.se.ifmo.tinder.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fabric_texture")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FabricTexture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fabric_texture_id")
    private Integer id;
    @Column(name = "fabric_texture_name")
    private String name;
}
