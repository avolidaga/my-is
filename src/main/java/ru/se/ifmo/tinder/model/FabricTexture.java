package ru.se.ifmo.tinder.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fabric_texture")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FabricTexture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fabric_texture_id")
    private Long id;

    @Column(name = "fabric_texture_name")
    private String name;

    @Column(name = "fabric_texture_description")
    private String description;
}
