package ru.se.info.tinder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "fabric_texture")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
