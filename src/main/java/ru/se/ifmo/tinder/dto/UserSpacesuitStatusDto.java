package ru.se.ifmo.tinder.dto;

import jakarta.persistence.*;
import lombok.*;
import ru.se.ifmo.tinder.model.FabricTexture;
import ru.se.ifmo.tinder.model.enums.Status;

@Entity
@Table(name = "user_spacesuit_data")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSpacesuitStatusDto {
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
    @Enumerated(EnumType.STRING)
    private Status status;
}
