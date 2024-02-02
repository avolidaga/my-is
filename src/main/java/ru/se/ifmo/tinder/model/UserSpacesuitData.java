package ru.se.ifmo.tinder.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_spacesuit_data")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSpacesuitData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_spacesuit_data_id")
    private Integer id;
    private Integer head;
    private Integer chest;
    private Integer waist;
    private Integer hips;
    private Integer foot_size;
    private Integer height;
    @Column(name = "fabric_texture_id")
    private Integer fabricTextureDto;
}
