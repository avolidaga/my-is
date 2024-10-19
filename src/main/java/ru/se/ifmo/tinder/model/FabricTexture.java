package ru.se.ifmo.tinder.model;

import jakarta.persistence.*;

@Entity
@Table(name = "fabric_texture")
public class FabricTexture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fabric_texture_id")
    private Long id;

    @Column(name = "fabric_texture_name")
    private String name;
}
