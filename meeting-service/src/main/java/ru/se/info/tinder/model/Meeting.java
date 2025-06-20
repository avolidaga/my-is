package ru.se.info.tinder.model;

import javax.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "user_spacesuit_data")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_spacesuit_data_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User ownerUser;
    private Integer head;
    private Integer chest;
    private Integer waist;
    private Integer hips;
    private Integer footSize;
    private Integer height;

    @ManyToOne
    @JoinColumn(name = "fabric_texture_id")
    private FabricTexture fabricTexture;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "spacesuitData")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<UserRequest> userRequestSet;
}
