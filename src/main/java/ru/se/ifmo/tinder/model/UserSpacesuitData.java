package ru.se.ifmo.tinder.model;

import jakarta.persistence.*;
import lombok.*;
import ru.se.ifmo.tinder.model.enums.RequestStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_spacesuit_data")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSpacesuitData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_spacesuit_data_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User ownerUser;
    private Integer head;
    private Integer chest;
    private Integer waist;
    private Integer hips;
    private Integer footSize;
    private Integer height;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @ManyToOne
    @JoinColumn(name = "fabric_texture_id")
    private FabricTexture fabricTexture;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
