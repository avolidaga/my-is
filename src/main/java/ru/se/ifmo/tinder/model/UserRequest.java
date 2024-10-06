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
@NamedQueries({
        @NamedQuery(name = "UserRequest.getAllUserRequestIds", query = "SELECT ur.user_request_id FROM UserRequest ur"),
        @NamedQuery(name = "UserRequest.getDeclinedUserRequestIds", query = "SELECT ur.user_request_id FROM UserRequest ur WHERE ur.status = 'DECLINED'"),
        @NamedQuery(name = "UserRequest.getReadyUserRequest", query = "SELECT ur.user_request_id FROM UserRequest ur WHERE ur.status = 'READY'"),
        @NamedQuery(name = "UserRequest.getInProgressUserRequest", query = "SELECT ur.user_request_id FROM UserRequest ur WHERE ur.status = 'IN PROGRES'"),
        @NamedQuery(name = "UserRequest.getListAllByUserRequestIdIn", query = "SELECT ur FROM UserRequest ur WHERE ur.user_request_id IN :idList")
})
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
