package ru.se.ifmo.tinder.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    @NotNull(message = "Head measurement must not be null")
    @Positive(message = "Head measurement must be a positive number")
    private Integer head;

    @NotNull(message = "Chest measurement must not be null")
    @Positive(message = "Chest measurement must be a positive number")
    private Integer chest;

    @NotNull(message = "Waist measurement must not be null")
    @Positive(message = "Waist measurement must be a positive number")
    private Integer waist;

    @NotNull(message = "Hips measurement must not be null")
    @Positive(message = "Hips measurement must be a positive number")
    private Integer hips;

    @NotNull(message = "Foot size must not be null")
    @Positive(message = "Foot size must be a positive number")
    private Integer foot_size;

    @NotNull(message = "Height must not be null")
    @Positive(message = "Height must be a positive number")
    private Integer height;

    @NotNull(message = "Status must not be null")
    @Enumerated(EnumType.STRING)
    private Status status;
}
