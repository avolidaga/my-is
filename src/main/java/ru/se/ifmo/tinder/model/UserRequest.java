package ru.se.ifmo.tinder.model;

import jakarta.persistence.*;
import lombok.*;
import ru.se.ifmo.tinder.model.enums.Status;


@Entity
@Table(name = "user_request")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_request_id")
    private Integer user_request_id;
    @ManyToOne
    @JoinColumn(name="user_spacesuit_data_id")
    private UserSpacesuitData userSpacesuitDataId;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

}
